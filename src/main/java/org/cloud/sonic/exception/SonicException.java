package org.cloud.sonic.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/10/10 11:57
 */
public class SonicException extends RuntimeException {

    private Object errorData;

    public SonicException(String message) {
        super(message);
    }

    public SonicException(String message, Object errorData) {
        super(message);
        this.errorData = errorData;
    }

    public SonicException(String message, Throwable cause) {
        super(message, cause);
    }

    @NonNull
    public HttpStatus getStatus() {
        return HttpStatus.OK;
    }

    @Nullable
    public Object getErrorData() {
        return errorData;
    }

    @NonNull
    public SonicException setErrorData(@Nullable Object errorData) {
        this.errorData = errorData;
        return this;
    }
}
