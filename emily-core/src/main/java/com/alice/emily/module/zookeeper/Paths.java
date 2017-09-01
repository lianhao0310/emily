package com.alice.emily.module.zookeeper;

import org.apache.curator.framework.CuratorFramework;

/**
 * Created by Lianhao on 2017/8/21.
 */
public class Paths {
    /**
     * Strip path information from a string. For example, given an input of
     * {@code /xd/path/location}, return {@code location}.
     *
     * @param path path string
     * @return string with path stripped
     */
    public static String stripPath(String path) {
        int i = path.lastIndexOf('/');
        return i > -1 ? path.substring(i + 1) : path;
    }

    /**
     * Return a string with the provided path elements separated by a slash {@code /}.
     * The leading slash is created if required.
     *
     * @param elements path elements
     * @return the full path
     */
    public static String build(String... elements) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            if (i == 0 && elements[i].charAt(0) != '/') {
                builder.append('/');
            }
            builder.append(elements[i]);
            if (i + 1 < elements.length) {
                builder.append('/');
            }
        }

        return builder.toString();
    }

    /**
     * Ensure the existence of the given path.
     *
     * @param client curator client
     * @param path   path to create, if needed
     */
    public static void ensurePath(CuratorFramework client, String path) {
        try {
            client.checkExists().creatingParentContainersIfNeeded().forPath(path);
        } catch (Exception e) {
            throw ZooKeeperUtils.wrapThrowable(e);
        }
    }
}
