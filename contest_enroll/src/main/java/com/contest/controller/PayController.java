package com.contest.controller;

import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.EnrollPaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/contest/pay")
public class PayController {

    @Resource
    private EnrollPaymentService enrollPaymentService;

    @RequestMapping("/alipay")
    @ResponseBody
    public ResultModel<String> alipay(
            @RequestParam("contestId") Long contestId,
            @CurrentUser UserDto userDto
    ) {
        return enrollPaymentService.createQrCode(contestId, userDto);
    }

    @GetMapping("/page/{template_id}")
    public String getPaymentPage(@PathVariable("template_id") String templateId, Model model){
        return enrollPaymentService.getPaymentPage(templateId,model);
    }

    @GetMapping("/page/success")
    public String getSuccessPage(){
        return "SuccessPage";
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    @ResponseBody
    public String successInvoke(@RequestParam("out_trade_no") String enrollId) {
        enrollPaymentService.successInvoke(enrollId);
        return enrollId;
    }
}

