package com.alice.emily.utils;

import com.alice.emily.exception.EuphoriaException;
import com.alice.emily.exception.EuphoriaResourceException;
import com.alice.emily.exception.EuphoriaServiceException;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

@UtilityClass
public class Errors {

    public static <E extends RuntimeException> E newException(Throwable t, Class<E> exceptionToThrow, String message) {
        try {
            E e;
            if (message != null) {
                e = AccessController.doPrivileged((PrivilegedExceptionAction<E>) () -> {
                    Constructor<E> constructor = ReflectionUtils.accessibleConstructor(exceptionToThrow, String.class, Throwable.class);
                    return BeanUtils.instantiateClass(constructor, message, exceptionToThrow);
                });
            } else {
                e = AccessController.doPrivileged((PrivilegedExceptionAction<E>) exceptionToThrow::newInstance);
            }
            if (t != null) e.initCause(t);
            return e;
        } catch (PrivilegedActionException ex) {
            throw new EuphoriaException(ex.getCause());
        }
    }

    public static <E extends RuntimeException> E newException(Throwable t, Class<E> exceptionToThrow, String format, Object... params) {
        return newException(t, exceptionToThrow, String.format(format, params));
    }

    public static EuphoriaException rethrow(Throwable t, String format, Object... params) {
        throw newException(t, EuphoriaException.class, format, params);
    }

    public static EuphoriaException rethrow(Throwable t) {
        throw newException(t, EuphoriaException.class, null);
    }

    public static EuphoriaException system(String format, Object... params) {
        return system(null, format, params);
    }

    public static EuphoriaException system(Throwable t, String format, Object... params) {
        return newException(t, EuphoriaException.class, format, params);
    }

    public static EuphoriaResourceException resource(String format, Object... params) {
        return resource(null, format, params);
    }

    public static EuphoriaResourceException resource(Throwable t, String format, Object... params) {
        return newException(t, EuphoriaResourceException.class, format, params);
    }

    public static EuphoriaServiceException service(String format, Object... params) {
        return service(null, format, params);
    }

    public static EuphoriaServiceException service(Throwable t, String format, Object... params) {
        return newException(t, EuphoriaServiceException.class, format, params);
    }

    public static UnsupportedOperationException unsupported(String format, Object... params) {
        return newException(null, UnsupportedOperationException.class, format, params);
    }
}
