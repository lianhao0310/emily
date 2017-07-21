package com.alice.emily.exception;

/**
 * Created by lianhao on 2016/12/10.
 */
public class EmilyResourceException extends EmilyException {

    public EmilyResourceException() {
    }

    public EmilyResourceException(String message) {
        super(message);
    }

    public EmilyResourceException(Throwable cause) {
        super(cause);
    }

    public EmilyResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
