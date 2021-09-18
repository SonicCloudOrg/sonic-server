package com.sonic.common.config;

import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
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
        logger.error(exception.getStackTrace().toString());
        if (exception instanceof MissingServletRequestParameterException) {
            return new RespModel(RespEnum.PARAMS_MISSING_ERROR);
        } else if (exception instanceof ConstraintViolationException) {
            return new RespModel(RespEnum.PARAMS_VIOLATE_ERROR);
        } else if (exception instanceof MethodArgumentNotValidException) {
            return new RespModel(RespEnum.PARAMS_NOT_VALID);
        } else if (exception instanceof HttpMessageNotReadableException) {
            return new RespModel(RespEnum.PARAMS_NOT_READABLE);
        } else {
            exception.printStackTrace();
            return new RespModel(RespEnum.UNKNOWN_ERROR);
        }
    }
}