package com.alice.emily.exception;

/**
 * Created by liupin on 2016/12/10.
 */
public class EmilyServiceException extends EmilyException {

    public EmilyServiceException() {
    }

    public EmilyServiceException(String message) {
        super(message);
    }

    public EmilyServiceException(Throwable cause) {
        super(cause);
    }

    public EmilyServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
