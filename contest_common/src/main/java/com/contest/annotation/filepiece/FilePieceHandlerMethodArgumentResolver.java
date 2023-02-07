package com.contest.annotation.filepiece;

import com.contest.dto.filesys.FileUploadDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class FilePieceHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FilePiece.class);
    }

    @Override
    public Object resolveArgument(
            @NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
            @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null){
            throw new NullPointerException();
        }
        return FileUploadDto.builder()
                .pieceInputStream(request.getInputStream())
                .sessionId(request.getHeader("sessionId"))
                .isLast(Boolean.parseBoolean(request.getHeader("isLast")))
                .build();
    }
}
