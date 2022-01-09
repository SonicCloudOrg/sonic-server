package org.cloud.sonic.common.exception;

import org.springframework.http.HttpStatus;

/**
 * BeanUtils exception.
 *
 * @author JayWenStar
 */
public class BeanToolException extends SonicException {

    public BeanToolException(String message) {
        super(message);
    }

    public BeanToolException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
