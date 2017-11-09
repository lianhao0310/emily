package com.alice.emily.exception;

/**
 * Created by liupin on 2016/12/10.
 */
public class EuphoriaResourceException extends EuphoriaException {

    public EuphoriaResourceException() {
    }

    public EuphoriaResourceException(String message) {
        super(message);
    }

    public EuphoriaResourceException(Throwable cause) {
        super(cause);
    }

    public EuphoriaResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
