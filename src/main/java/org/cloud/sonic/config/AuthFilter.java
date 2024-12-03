/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
            if (exchange.getRequest().getURI().getPath().contains(white)) {
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
        // verify token
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