package com.sonic.gateway.config;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.common.tools.JWTTokenTool;
import com.sonic.gateway.tools.RedisTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Value("${filter.white-list}")
    private List<String> whiteList;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        for (String white : whiteList) {
            if (exchange.getRequest().getURI().toString().contains(white)) {
                return chain.filter(exchange);
            }
        }
        String token = exchange.getRequest().getHeaders().getFirst("sonicToken");
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        DataBuffer buffer = sendResp(response);
        if (token == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeWith(Mono.just(buffer));
        }
        String username = JWTTokenTool.getUserName(token);
        if (username != null && username.length() > 0) {
            String redisToken;
            Object redisTokenObject = RedisTool.get("sonic:user:" + username);
            if (redisTokenObject == null) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.writeWith(Mono.just(buffer));
            }
            redisToken = redisTokenObject.toString();
            if (!redisToken.equals(token)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.writeWith(Mono.just(buffer));
            } else {
                RedisTool.expire("sonic:user:" + username, 7);
            }
        } else {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeWith(Mono.just(buffer));
        }
        // 验证 token
        if (!JWTTokenTool.verify(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private DataBuffer sendResp(ServerHttpResponse response) {
        JSONObject result = (JSONObject) JSONObject.toJSON(new RespModel(RespEnum.UNAUTHORIZED));
        DataBuffer buffer = response.bufferFactory().wrap(result.toJSONString().getBytes(StandardCharsets.UTF_8));
        return buffer;
    }
}