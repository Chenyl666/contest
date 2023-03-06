package com.contest.service;

import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface EnrollPaymentService {
    /**
     * 创建订单，并生成二维码表单
     * */
    public ResultModel<String> createQrCode(Long contestId, UserDto userDto);

    /**
     * 支付成功回调
     * */
    public ResultModel<String> successInvoke(String enrollId);

    /**
     * 获取支付页面
     * */
    public String getPaymentPage(String templateId,Model model);
}
