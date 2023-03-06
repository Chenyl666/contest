package com.contest.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {

    @Value("${alipay.appid}")
    String APP_ID;
    @Value("${alipay.app_private_key}")
    String APP_PRIVATE_KEY;
    @Value("${alipay.charset}")
    String CHARSET;
    @Value("${alipay.alipay_public_key}")
    String ALIPAY_PUBLIC_KEY;
    //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
    @Value("${alipay.gateway_url}")
    String GATEWAY_URL;
    @Value("${alipay.format}")
    String FORMAT;
    @Value("${alipay.sign_type}")
    //签名方式
    String SIGN_TYPE;
    @Value("${alipay.notify_url}")

    @Bean
    public AlipayClient alipayClient() {

        return new DefaultAlipayClient(
                GATEWAY_URL,
                APP_ID,
                APP_PRIVATE_KEY,
                FORMAT,
                CHARSET,
                ALIPAY_PUBLIC_KEY,
                SIGN_TYPE
        );
    }

}
