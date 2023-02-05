package com.contest.config;

import com.contest.annotation.currentuser.CurrentUserHandlerMethodArgumentResolver;
import com.contest.annotation.filepiece.FilePieceHandlerMethodArgumentResolver;
import com.contest.annotation.uploadrequest.UploadRequestHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserHandlerMethodArgumentResolver());
        resolvers.add(new FilePieceHandlerMethodArgumentResolver());
        resolvers.add(new UploadRequestHandlerMethodArgumentResolver());
    }
}
