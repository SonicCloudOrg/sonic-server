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
package org.cloud.sonic.common.config;

import jakarta.annotation.Resource;
import org.cloud.sonic.common.http.RespModel;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Locale;

/**
 * @author JayWenStar, Eason
 * @date 2022/4/11 1:59 上午
 */
@ControllerAdvice
public class CommonResultControllerAdvice implements ResponseBodyAdvice<Object> {
    @Resource
    private MessageSource messageSource;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        MappingJacksonValue container = getOrCreateContainer(body);

        String l = request.getHeaders().getFirst("Accept-Language");
        // Get return body
        Object returnBody = container.getValue();

        if (returnBody instanceof RespModel) {
            RespModel<?> baseResponse = (RespModel) returnBody;
            process(l, baseResponse, messageSource);
        }
        return container;
    }

    public static RespModel process(String l, RespModel respModel, MessageSource messageSource) {
        String language = "en_US";
        if (l != null) {
            language = l;
        }
        String[] split = language.split("_");
        Locale locale;
        if (split.length >= 2) {
            locale = new Locale(split[0], split[1]);
        } else {
            locale = new Locale("en", "US");
        }
        respModel.setMessage(messageSource.getMessage(respModel.getMessage(), new Object[]{}, locale));
        return respModel;
    }

    private MappingJacksonValue getOrCreateContainer(Object body) {
        return (body instanceof MappingJacksonValue ? (MappingJacksonValue) body : new MappingJacksonValue(body));
    }
}
