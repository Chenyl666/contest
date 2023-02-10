package com.contest.annotation.filepiece;

import com.contest.dto.filesys.FileUploadDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Optional;

public class FilePieceHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FilePiece.class);
    }

    @Override
    public Object resolveArgument(
            @NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
            @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        MultipartRequest multipartRequest = webRequest.getNativeRequest(MultipartRequest.class);
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if(multipartRequest == null || httpServletRequest == null){
            throw new NullPointerException();
        }
        Optional<MultipartFile> filePieceOptional = Optional.ofNullable(multipartRequest.getFile("filePiece"));
        InputStream in = null;
        if(filePieceOptional.isPresent()){
            in = filePieceOptional.get().getInputStream();
        }
        return FileUploadDto.builder()
                .pieceInputStream(in)
                .sessionId(httpServletRequest.getParameter("sessionId"))
                .isLast(Boolean.parseBoolean(httpServletRequest.getParameter("isLast")))
                .build();
    }
}
