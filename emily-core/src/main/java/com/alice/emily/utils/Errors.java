package com.alice.emily.utils;

import com.alice.emily.exception.EmilyException;
import com.alice.emily.exception.EmilyResourceException;
import com.alice.emily.exception.EmilyServiceException;
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
            throw new EmilyException(ex.getCause());
        }
    }

    public static <E extends RuntimeException> E newException(Throwable t, Class<E> exceptionToThrow, String format, Object... params) {
        return newException(t, exceptionToThrow, String.format(format, params));
    }

    public static EmilyException rethrow(Throwable t, String format, Object... params) {
        throw newException(t, EmilyException.class, format, params);
    }

    public static EmilyException rethrow(Throwable t) {
        throw newException(t, EmilyException.class, null);
    }

    public static EmilyException system(String format, Object... params) {
        return system(null, format, params);
    }

    public static EmilyException system(Throwable t, String format, Object... params) {
        return newException(t, EmilyException.class, format, params);
    }

    public static EmilyResourceException resource(String format, Object... params) {
        return resource(null, format, params);
    }

    public static EmilyResourceException resource(Throwable t, String format, Object... params) {
        return newException(t, EmilyResourceException.class, format, params);
    }

    public static EmilyServiceException service(String format, Object... params) {
        return service(null, format, params);
    }

    public static EmilyServiceException service(Throwable t, String format, Object... params) {
        return newException(t, EmilyServiceException.class, format, params);
    }

    public static UnsupportedOperationException unsupported(String format, Object... params) {
        return newException(null, UnsupportedOperationException.class, format, params);
    }
}
