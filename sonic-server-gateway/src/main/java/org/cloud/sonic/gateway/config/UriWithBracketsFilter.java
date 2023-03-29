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

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * 针对 <a href="https://github.com/spring-cloud/spring-cloud-gateway/issues/2065">spring-cloud/spring-cloud-gateway#2065</a>
 * 问题的临时缓解方案，向前兼容url查询参数出现未编码的 [ ] 字符，但url中还同时存在其他已按url编码的字符的情况。
 */
@Component
public class UriWithBracketsFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String query = exchange.getRequest().getURI().getRawQuery();
        if (query != null && query.contains("%") && (query.contains("[") || query.contains("]"))) {
            try {
                String badUrl = exchange.getAttributes().get(GATEWAY_REQUEST_URL_ATTR).toString();
                String fineUrl = badUrl.substring(0, badUrl.indexOf('?') + 1) +
                        query.replace("[", "%5B").replace("]", "%5D");
                exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, new URI(fineUrl));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return ReactiveLoadBalancerClientFilter.LOAD_BALANCER_CLIENT_FILTER_ORDER + 1;
    }
}
