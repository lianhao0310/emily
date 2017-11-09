package com.alice.emily.zookeeper;

import com.alice.emily.exception.EuphoriaException;

/**
 * Runtime exception used to wrap native ZooKeeper checked exceptions thrown while
 * accessing ZooKeeper nodes.
 *
 */
@SuppressWarnings("serial")
public class ZooKeeperAccessException extends EuphoriaException {

    public ZooKeeperAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}