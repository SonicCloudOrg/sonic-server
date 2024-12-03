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

import jakarta.validation.ConstraintViolationException;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ZhouYiXun
 * @des 全局异常拦截
 * @date 2021/8/15 18:26
 */
@RestControllerAdvice
@Order(1)
public class GlobalWebException {
    private final Logger logger = LoggerFactory.getLogger(GlobalWebException.class);

    @ExceptionHandler(Exception.class)
    public RespModel ErrHandler(Exception exception) {
        logger.error(exception.getMessage());
        if (exception instanceof MissingServletRequestParameterException) {
            return new RespModel(RespEnum.PARAMS_MISSING_ERROR);
        } else if (exception instanceof ConstraintViolationException) {
            return new RespModel(RespEnum.PARAMS_VIOLATE_ERROR);
        } else if (exception instanceof MethodArgumentNotValidException) {
            return new RespModel(RespEnum.PARAMS_NOT_VALID);
        } else if (exception instanceof HttpMessageNotReadableException) {
            return new RespModel(RespEnum.PARAMS_NOT_READABLE);
        } else if (exception instanceof SonicException) {
            return new RespModel(4006, exception.getMessage());
        } else {
            exception.printStackTrace();
            return new RespModel(RespEnum.UNKNOWN_ERROR);
        }
    }
}
