package com.alice.emily.exception;

/**
 * Created by wyx on 12/9/15.
 */
public class EuphoriaException extends RuntimeException {

    public EuphoriaException() { }

    public EuphoriaException(String message) {
        super(message);
    }

    public EuphoriaException(Throwable cause) {
        super(cause);
    }

    public EuphoriaException(String message, Throwable cause) {
        super(message, cause);
    }

}
