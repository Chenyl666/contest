package com.contest.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.async.provider.NotifySendingProvider;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.contest.ContestEnrollEntity;
import com.contest.entity.notify.NotifyMessageEntity;
import com.contest.enu.ContestStatus;
import com.contest.mapper.ContestEnrollMapper;
import com.contest.result.ResultModel;
import com.contest.service.ContestDetailService;
import com.contest.service.EnrollPaymentService;
import com.contest.util.DateUtils;
import com.contest.util.SnowMaker;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class EnrollPaymentServiceImpl implements EnrollPaymentService {

    @Value("${alipay.enroll_payment.return_url}")
    private String RETURN_URL;

    @Value("${alipay.enroll_payment.notify_url}")
    private String NOTIFY_URL;

    @Value("${alipay.charset}")
    String CHARSET;

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private ContestDetailService contestDetailService;

    @Resource
    private ContestEnrollMapper contestEnrollMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private NotifySendingProvider notifySendingProvider;

    private static final SnowMaker snowMaker = new SnowMaker(1);

    private final String URL_PREFIX = "http://localhost:8080";

    /**
     * 创建订单，并生成二维码表单
     */
    @SneakyThrows
    @Transactional
    @Override
    public ResultModel<String> createQrCode(Long contestId, UserDto userDto) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //在公共参数中设置回跳和通知地址
        request.setReturnUrl(RETURN_URL);
        request.setNotifyUrl(NOTIFY_URL);
        ContestDetailEntity contestDetailEntity = contestDetailService.getById(contestId);
        if (contestDetailEntity == null) {
            return ResultModel.buildFailResultModel("当前时间不在报名时间内！");
        }
        boolean enrollTimeout = !DateUtils.dateRangeCompare(
                contestDetailEntity.getEnrollStartTime(),
                contestDetailEntity.getEnrollEndTime(),
                new Date(System.currentTimeMillis())
        );
        if (enrollTimeout) {
            return ResultModel.buildFailResultModel(null, "当前时间不在报名时间内！");
        }
        Optional<ContestEnrollEntity> contestEnrollOptional = Optional.ofNullable(contestEnrollMapper.selectOne(
                new QueryWrapper<ContestEnrollEntity>()
                        .eq("contest_id", contestId)
                        .eq("user_id", userDto.getUserId())
        ));
        contestEnrollOptional.ifPresent(contestEnroll -> {
            contestEnrollMapper.deleteById(contestEnroll.getEnrollId());
        });
        ContestEnrollEntity contestEnrollEntity = ContestEnrollEntity
                .builder()
                .userId(userDto.getUserId())
                .enrollId(snowMaker.nextId())
                .pay(false)
                .contestId(contestId)
                .createdDate(new Date())
                .updatedDate(new Date())
                .build();
        contestEnrollMapper.insert(contestEnrollEntity);
        //商户订单号，商户网站订单系统中唯一订单号，必填
        String outTradeNo = Long.toString(contestEnrollEntity.getEnrollId());
        //付款金额，必填
        String totalAmount = contestDetailEntity.getContestPrice();
        //订单名称，必填
        String subject = contestDetailEntity.getContestSubject().concat("报名费用");
        //商品描述，可空
        String body = "若支付成功，您则将获得参赛资格！";
        Map<String, String> bizContent = new HashMap<>();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("total_amount", totalAmount);
        bizContent.put("subject", subject);
        bizContent.put("body", body);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        String templateId = String.valueOf(snowMaker.nextId());
        bizContent.put("templateId", templateId);
        request.setBizContent(JSON.toJSONString(bizContent));
        String form = null;
        String url = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
            redisTemplate.opsForValue().set(templateId, form);
            redisTemplate.expire(templateId, 30, TimeUnit.MINUTES);
            url = URL_PREFIX.concat("/contest/pay/page/").concat(templateId);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return ResultModel.buildSuccessResultModel(null, url);
    }

    /**
     * 支付成功回调
     */
    @Override
    public ResultModel<String> successInvoke(String enrollId) {
        Optional<ContestEnrollEntity> contestEnrollEntityOptional = Optional.ofNullable(
                contestEnrollMapper.selectById(enrollId)
        );
        contestEnrollEntityOptional.ifPresent(contestEnrollEntity -> {
            contestEnrollEntity.setPay(true);
            contestEnrollMapper.updateById(contestEnrollEntity);
            sendEnrollSuccessNotifyMessage(contestEnrollEntity);
        });

        return ResultModel.buildSuccessResultModel("支付成功！");
    }

    /**
     * 获取支付页面
     * */
    @Override
    public String getPaymentPage(String templateId, Model model) {
        Object templateCode = redisTemplate.opsForValue().get(templateId);
        if (templateCode == null) {
            return "NotFound";
        }
        model.addAttribute("templateCode", templateCode.toString());
        return "Payment";
    }

    private void sendEnrollSuccessNotifyMessage(ContestEnrollEntity contestEnrollEntity){
        ContestDetailEntity contestDetailEntity = contestDetailService.getById(contestEnrollEntity.getContestId());
        System.out.println(contestDetailEntity);
        if(contestDetailEntity != null){
            NotifyMessageEntity notifyMessageEntity = NotifyMessageEntity
                    .builder()
                    .messageId(snowMaker.nextId())
                    .messageTitle("报名成功通知")
                    .messageContent("恭喜，您已成功报名网络竞赛："
                            .concat(contestDetailEntity.getContestSubject())
                            .concat("。比赛时间为：")
                            .concat(DateUtils.getTimeStr(contestDetailEntity.getContestStartTime()))
                            .concat("~")
                            .concat(DateUtils.getTimeStr(contestDetailEntity.getContestEndTime()))
                            .concat("，请及时参加比赛！")
                    )
                    .hasRead(false)
                    .receiver(contestEnrollEntity.getUserId())
                    .createdDate(new Date())
                    .createdBy("system")
                    .build();
            notifySendingProvider.sendNotifyMessage(notifyMessageEntity);
        }

    }

}
