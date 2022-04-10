/*
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.gateway.config;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
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
    @Autowired
    private JWTTokenTool jwtTokenTool;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        for (String white : whiteList) {
            if (exchange.getRequest().getURI().toString().contains(white)) {
                return chain.filter(exchange);
            }
        }
        String token = exchange.getRequest().getHeaders().getFirst("SonicToken");
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        DataBuffer buffer = sendResp(response);
        if (token == null) {
            return response.writeWith(Mono.just(buffer));
        }
        // 验证 token
        if (!jwtTokenTool.verify(token)) {
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