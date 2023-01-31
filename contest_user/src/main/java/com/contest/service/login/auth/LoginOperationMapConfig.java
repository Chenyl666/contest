package com.contest.service.login.auth;

import com.contest.service.login.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginOperationMapConfig {
    @Resource(type = UserPasswordLoginOperation.class)
    LoginOperation userPasswordLoginOperation;

    @Resource(type = EmailPasswordLoginOperation.class)
    LoginOperation emailPasswordLoginOperation;

    @Resource(type = EmailCodeLoginOperation.class)
    LoginOperation emailCodeLoginOperation;

    @Bean("loginOperationMap")
    public Map<Integer,LoginOperation> loginOperationMap(){
        Map<Integer, LoginOperation> loginOperationMap = new HashMap<>();
        loginOperationMap.put(LoginOperation.LOGIN_BY_USER_PASSWORD,userPasswordLoginOperation);
        loginOperationMap.put(LoginOperation.LOGIN_BY_EMAIL_PASSWORD,emailPasswordLoginOperation);
        loginOperationMap.put(LoginOperation.LOGIN_BY_EMAIL_CODE,emailCodeLoginOperation);
        return loginOperationMap;
    }

}
