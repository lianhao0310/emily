package com.alice.emily.module.zookeeper;

import com.alice.emily.exception.EmilyException;

/**
 * Created by Lianhao on 2017/8/21.
 */
@SuppressWarnings("serial")
public class ZooKeeperAccessException extends EmilyException {
    public ZooKeeperAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
