package com.alice.emily.utils.logging;

import lombok.experimental.Delegate;
import org.apache.logging.log4j.LogManager;

public class DelegateLogger implements Logger {

    private final String name;
    private volatile org.apache.logging.log4j.Logger logger;

    public DelegateLogger(String name) {
        this.name = name;
    }


    @Delegate
    protected org.apache.logging.log4j.Logger delegate() {
        if (logger == null) {
            synchronized (this) {
                if (logger == null) {
                    logger = LogManager.getLogger(name);
                }
            }
        }
        return logger;
    }
}