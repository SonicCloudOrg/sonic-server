package org.cloud.sonic.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author JayWenStar
 */
public class ServerErrorException extends SonicException {

    public ServerErrorException(String message) {
        super(message);
    }

    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
