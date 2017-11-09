package com.alice.emily.exception;

/**
 * Created by lianhao on 2017/6/21.
 */
public class UnsupportedClassTypeException extends EuphoriaException {

    public UnsupportedClassTypeException() {
        super();
    }

    public UnsupportedClassTypeException(String message) {
        super(message);
    }

    public UnsupportedClassTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedClassTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
