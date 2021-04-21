package com.example.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Value("${token.secret}")
    private String tokenSecret;

    public AuthorizationHeaderFilter() {
        super(AuthorizationHeaderFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 요청 정보를 꺼내온다.
            ServerHttpRequest request = exchange.getRequest();

            // Header 에 인증 정보가 존재하는지 검증
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);   // 401 에러

            // Header 에서 인증 정보를 가져온다.
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            // Header 에서 Bearer Token 정보를 가져온다.
            String jwt = authorizationHeader.replace("Bearer", "");

            // 정상적인 토큰 값인지 검증
            if (!isJwtValid(jwt))
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);

            return chain.filter(exchange);
        };
    }

    /**
     * 에러 반환
     * - Mono 는 단일값 / Flux 는 멀티값
     */
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);

        return response.setComplete();
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;
        String subject = null;

        // 토큰 추출 및 복호화
        try {
            subject = Jwts.parser().setSigningKey(tokenSecret)
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
        } catch (Exception ex) {
            returnValue = false;
        }

        if (subject == null || subject.isEmpty())
            returnValue = false;

        return returnValue;
    }

    public static class Config {

    }
}
