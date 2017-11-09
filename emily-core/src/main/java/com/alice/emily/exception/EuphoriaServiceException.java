package com.alice.emily.exception;

/**
 * Created by liupin on 2016/12/10.
 */
public class EuphoriaServiceException extends EuphoriaException {

    public EuphoriaServiceException() {
    }

    public EuphoriaServiceException(String message) {
        super(message);
    }

    public EuphoriaServiceException(Throwable cause) {
        super(cause);
    }

    public EuphoriaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
