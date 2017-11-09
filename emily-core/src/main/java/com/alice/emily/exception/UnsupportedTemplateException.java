package com.alice.emily.exception;

/**
 * Created by lianhao on 2017/5/9.
 */
public class UnsupportedTemplateException extends EuphoriaServiceException {

    public UnsupportedTemplateException() {
        super();
    }

    public UnsupportedTemplateException(String message) {
        super(message);
    }

    public UnsupportedTemplateException(Throwable cause) {
        super(cause);
    }

    public UnsupportedTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
