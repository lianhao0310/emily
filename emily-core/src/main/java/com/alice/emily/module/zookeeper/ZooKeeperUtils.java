package com.alice.emily.module.zookeeper;


import com.alice.emily.utils.JSONUtils;
import lombok.SneakyThrows;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

public abstract class ZooKeeperUtils {

    /**
     * Convert a map of string key/value pairs to a JSON string in a byte array.
     *
     * @param map map to convert
     * @return byte array
     */
    @SneakyThrows
    public static byte[] mapToBytes(Map<String, String> map) {
        return JSONUtils.getMapper().writeValueAsBytes(map);
    }

    /**
     * Convert a byte array containing a JSON string to a map of key/value pairs.
     *
     * @param bytes byte array containing the key/value pair strings
     * @return a new map instance containing the key/value pairs
     */
    public static Map<String, String> bytesToMap(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return Collections.emptyMap();
        }
        try {
            return JSONUtils.getMapper().reader().readValue(bytes);
        } catch (Exception e) {
            String contents;
            try {
                contents = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException uue) {
                contents = "Could not read content due to " + uue;
            }
            throw new RuntimeException("Error parsing JSON string: " + contents, e);
        }
    }

    /**
     * Utility method to wrap a Throwable in a {@link ZooKeeperAccessException}.
     *
     * @param t the throwable
     */
    public static RuntimeException wrapThrowable(Throwable t) {
        return wrapThrowable(t, null);
    }

    /**
     * Utility method to wrap a Throwable in a {@link ZooKeeperAccessException}.
     *
     * @param t       the throwable
     * @param message use this message if not null
     */
    public static RuntimeException wrapThrowable(Throwable t, String message) {
        if (message != null) {
            return new ZooKeeperAccessException(message, t);
        }
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        } else {
            return new ZooKeeperAccessException(t.getMessage(), t);
        }
    }

    /**
     * Throw a wrapped exception ignoring specific Exception types, if any.
     *
     * @param t       the throwable
     * @param ignored a varargs list of ignored exception types
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void wrapAndThrowIgnoring(Throwable t, Class... ignored) {
        if (ignored != null) {
            for (Class<? extends Exception> e : ignored) {
                if (e.isAssignableFrom(t.getClass())) {
                    return;
                }
            }
        }
        throw wrapThrowable(t);
    }

    /**
     * Utility method to log {@link org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent events}.
     *
     * @param logger logger to write to
     * @param event  event to log
     */
    public static void logCacheEvent(Logger logger, PathChildrenCacheEvent event) {
        ChildData data = event.getData();
        StringBuilder builder = new StringBuilder();
        builder.append("Path cache event: ");
        if (data != null && data.getPath() != null) {
            builder.append("path=").append(data.getPath()).append(", ");
        }
        builder.append("type=").append(event.getType());

        if (EnumSet.of(PathChildrenCacheEvent.Type.CONNECTION_SUSPENDED,
                PathChildrenCacheEvent.Type.CONNECTION_LOST).contains(event.getType())) {
            logger.warn(builder.toString());
        } else {
            logger.info(builder.toString());
        }

        if (data != null && logger.isTraceEnabled()) {
            String content;
            byte[] bytes = data.getData();
            if (bytes == null || bytes.length == 0) {
                content = "empty";
            } else {
                try {
                    content = new String(data.getData(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    content = "Could not convert content to UTF-8: " + e.toString();
                }
            }
            logger.trace("Data for path {}: {}", data.getPath(), content);
        }
    }

    /**
     * Return the full stack trace for a Throwable.
     *
     * @param t throwable for which to obtain the full stack trace
     * @return string containing the full stack trace
     */
    public static String getStackTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);

        return stringWriter.toString();
    }

}
