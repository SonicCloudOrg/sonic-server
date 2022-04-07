/**
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
package org.cloud.sonic.common.config;

import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @author ZhouYiXun
 * @des 全局异常拦截
 * @date 2021/8/15 18:26
 */
@RestControllerAdvice
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