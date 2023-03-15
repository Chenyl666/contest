package com.contest;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.JWT;
import com.contest.sso.JwtUtil;
import com.contest.sso.TokenPayload;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class LoginTest {

    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzQxOTc4NDcsImN1cnJlbnRfdXNlciI6IntcImV4cGlyZVwiOmZhbHNlLFwiZXhwaXJlVGltZXJcIjoxNjc0MTk3ODQ3OTYyLFwiaXBBZGRyZXNzXCI6XCIwOjA6MDowOjA6MDowOjFcIixcImxvZ2luVGltZXJcIjoxNjczOTM4NjQ3OTYyLFwidXNlckR0b1wiOntcInVzZXJFbWFpbFwiOlwiNTU0MDM2NDMzQHFxLmNvbVwiLFwidXNlcklkXCI6XCJjaGVueWxcIixcInVzZXJOYW1lXCI6XCLmlrDnlKjmiLdjaGVueWxcIixcInVzZXJQaG9uZVwiOlwiMTg5MjcwNzMzNjJcIixcInVzZXJQaWNcIjpcImh0dHA6Ly9sb2NhbGhvc3Q6Nzc4OC9maWxlc3lzL3VzZXIvcGljL2RlZmF1bHRcIixcInVzZXJUeXBlXCI6Mn19In0.Fi06IzZdWYx2w52aBBMV4BWpfRSmj5PTIChyWB9wCyE";

    @Test
    public void loginTest(){
        String userJson = JWT.decode(token).getClaims().get("current_user").asString();
        TokenPayload tokenPayload = JSON.parseObject(userJson, TokenPayload.class);
    }

}
