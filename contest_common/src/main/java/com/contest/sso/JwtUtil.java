package com.contest.sso;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.contest.dto.user.UserDto;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static sun.security.x509.X509CertImpl.SIGNATURE;

/**
 * 基于JWT实现SSO单点登录
 * token由三部分组成：header(头部)、payload(载荷)、signature(签名)
 * */
public class JwtUtil {

    /**
     * 生成token
     * */
    public static String generateToken(UserDto userDto,String ipAddress){
        TokenPayload tokenPayload = new TokenPayload(userDto, ipAddress, System.currentTimeMillis());
        Map<String, String> payload = new HashMap<>();
        payload.put("current_user", JSON.toJSONString(tokenPayload));
        JWTCreator.Builder jwtCreatorBuilder = JWT.create();
        payload.forEach(jwtCreatorBuilder::withClaim);
        jwtCreatorBuilder.withExpiresAt(new Date(tokenPayload.getExpireTimer()));
        return jwtCreatorBuilder.sign(Algorithm.HMAC256(SIGNATURE));
    }

    /**
     * 验证token是否有效
     * */
    public static boolean verify(String token){
        try{
            verifyToken(token);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public static UserDto getUserByToken(String token){
        return JSON.parseObject(
                JWT.decode(token).getClaims().get("current_user").asString(), TokenPayload.class
        ).getUserDto();
    }

    /**
     * JWT token验证
     * */
    private static void verifyToken(String token){
        JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }
}
