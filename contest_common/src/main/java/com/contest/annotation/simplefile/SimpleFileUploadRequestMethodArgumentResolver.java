package com.contest.annotation.simplefile;

import com.contest.dto.filesys.SimpleFileUploadDto;
import com.contest.dto.user.UserDto;
import com.contest.sso.JwtUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class SimpleFileUploadRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SimpleFileUploadRequest.class);
    }

    @Override
    public Object resolveArgument(
            @NotNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null){
            return null;
        }
        SimpleFileUploadDto simpleFileUploadDto = SimpleFileUploadDto
                .builder()
                .fileName(request.getHeader("fileName"))
                .publicPerm(Boolean.parseBoolean(request.getHeader("publicPerm")))
                .in(request.getInputStream())
                .fileMd5(request.getHeader("fileMd5"))
                .build();
        Optional<UserDto> userDtoOptional = Optional.ofNullable(JwtUtil.getUserByToken(request.getHeader("token")));
        userDtoOptional.ifPresent(userDto -> simpleFileUploadDto.setUserId(userDto.getUserId()));
        return simpleFileUploadDto;
    }
}
