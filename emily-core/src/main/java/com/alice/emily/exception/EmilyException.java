package com.alice.emily.exception;

/**
 * Created by wyx on 12/9/15.
 */
public class EmilyException extends RuntimeException {

    public EmilyException() { }

    public EmilyException(String message) {
        super(message);
    }

    public EmilyException(Throwable cause) {
        super(cause);
    }

    public EmilyException(String message, Throwable cause) {
        super(message, cause);
    }

}
