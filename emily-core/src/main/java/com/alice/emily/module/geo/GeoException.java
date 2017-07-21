package com.alice.emily.module.geo;

import com.alice.emily.exception.EmilyException;

/**
 * Created by liupin on 2017/1/5.
 */
public class GeoException extends EmilyException {

    public GeoException() {
    }

    public GeoException(String message) {
        super(message);
    }

    public GeoException(Throwable cause) {
        super(cause);
    }

    public GeoException(String message, Throwable cause) {
        super(message, cause);
    }
}
