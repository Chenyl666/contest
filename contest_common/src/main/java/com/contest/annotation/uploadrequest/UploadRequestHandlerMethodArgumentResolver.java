package com.contest.annotation.uploadrequest;

import com.alibaba.fastjson2.JSON;
import com.contest.dto.filesys.FileUploadRequest;
import com.contest.dto.user.UserDto;
import com.contest.sso.JwtUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

public class UploadRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(@NotNull MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UploadRequest.class);
    }

    @Override
    public Object resolveArgument(
            @NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
            @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null){
            throw new NullPointerException();
        }
        BufferedReader reader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            stringBuilder.append(line);
        }
        String json = stringBuilder.toString();
        FileUploadRequest fileUploadRequest = JSON.parseObject(json, FileUploadRequest.class);
        String token = request.getHeader("token");
        UserDto userDto = JwtUtil.getUserByToken(token);
        if(userDto != null){
            fileUploadRequest.setUserId(userDto.getUserId());
        }
        return fileUploadRequest;
    }
}
