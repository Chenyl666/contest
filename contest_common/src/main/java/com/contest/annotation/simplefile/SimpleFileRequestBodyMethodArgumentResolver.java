package com.contest.annotation.simplefile;

import com.contest.dto.filesys.SimpleFileUploadDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class SimpleFileRequestBodyMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SimpleFileRequestBody.class);
    }

    @Override
    public Object resolveArgument(
            @NotNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        MultipartRequest multipartRequest = webRequest.getNativeRequest(MultipartRequest.class);
        if(request == null || multipartRequest == null){
            throw new NullPointerException();
        }
        byte[] fileBytes = Objects.requireNonNull(multipartRequest.getFile("file")).getBytes();
        return SimpleFileUploadDto
                .builder()
                .fileName(request.getParameter("fileName"))
                .publicPerm(Boolean.parseBoolean(request.getParameter("publicPerm")))
                .fileBytes(fileBytes)
                .timeLimit(Boolean.parseBoolean(request.getParameter("timeLimit")))
                .build();
    }
}
