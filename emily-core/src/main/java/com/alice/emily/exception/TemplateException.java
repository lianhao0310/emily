package com.alice.emily.exception;

/**
 * Created by lianhao on 2017/5/9.
 */
public class TemplateException extends EmilyServiceException {

    public TemplateException() {
        super();
    }

    public TemplateException(String message) {
        super(message);
    }

    public TemplateException(Throwable cause) {
        super(cause);
    }

    public TemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
