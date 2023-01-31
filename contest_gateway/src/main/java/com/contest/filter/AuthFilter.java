package com.contest.filter;

import com.contest.config.SkippingPathConfig;
import com.contest.sso.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Token权限认证过滤器
 * */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private SkippingPathConfig skippingPathConfig;

    /**
     * Token权限认证过滤器
     * 1. 判断请求是否需要token
     * 2. 验证token
     * 3. 对通过token验证的请求，进行转发
     * 4. 对不通过token验证的请求，响应401
     * */
    @Override
    @SuppressWarnings("all")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if(isSkippingAuth(request.getURI().getPath())){
            log.info("The request is able skipping!");
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst("token");
        if(token != null && JwtUtil.verify(token)){
            log.info("token authenticate pass!");
            return chain.filter(exchange);
        }
        ServerHttpResponse resp = exchange.getResponse();
        resp.setRawStatusCode(401);
        log.info("token authenticate fail!");
        return resp.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Boolean isSkippingAuth(String path){
        for (String uri : skippingPathConfig.getSkipping()) {
            if(path.contains(uri)){
                return true;
            }
        }
        return false;
    }

}
