package com.alice.emily.utils;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Created by wyx on 4/5/16.
 */
public class Reflection {

    private Reflection() { }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    public static <T> T cast(Object obj, Class<T> type) {
        if (type.isPrimitive()) {
            type = cast(ClassUtils.primitiveToWrapper(type));
        }
        return type.cast(obj);
    }

    public static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
        return com.google.common.reflect.Reflection.newProxy(interfaceType, handler);
    }

    public static boolean isClassPresent(String canonicalName) {
        try {
            Class.forName(canonicalName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static <T> T readField(Object target, String fieldName) {
        try {
            return cast(FieldUtils.readField(target, fieldName, true));
        } catch (Exception e) {
            throw Errors.rethrow(e);
        }
    }

    public static <T> T newInstance(String className) throws ClassNotFoundException {
        ClassLoader loader = MoreObjects.firstNonNull(
                Thread.currentThread().getContextClassLoader(),
                Reflection.class.getClassLoader());
        Type type = loader.loadClass(className);
        return newInstance(type);
    }

    public static <T> T newInstance(Type type) {
        if (type == null) return null;
        Class<T> tClass = getRawType(type);
        if (List.class.isAssignableFrom(tClass)) {
            return cast(new ArrayList());
        }
        if (ConcurrentMap.class.isAssignableFrom(tClass)) {
            return cast(new ConcurrentHashMap<>());
        }
        if (Map.class.isAssignableFrom(tClass)) {
            return cast(new HashMap<>());
        }
        if (Set.class.isAssignableFrom(tClass)) {
            return cast(new HashSet());
        }
        try {
            return cast(tClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw Errors.rethrow(e);
        }
    }

    public static <T> T newInstance(Class<T> type, Object... params) {
        try {
            Object o = ConstructorUtils.invokeConstructor(type, params);
            return cast(o);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw Errors.rethrow(e);
        }
    }

    public static boolean isArray(Type type) {
        return type != null && type instanceof Class && ((Class) type).isArray();
    }

    public static boolean isCollection(Type type) {
        if (type == null) return false;
        if (type instanceof ParameterizedType) {
            type = ((ParameterizedType) type).getRawType();
        }
        return type instanceof Class && Collection.class.isAssignableFrom((Class<?>) type);
    }

    public static boolean isPrimitive(Type type) {
        Class<?> rawType = getRawType(type);
        return rawType != null && rawType.isPrimitive();
    }

    public static Type getElementType(Type type) {
        if (type == null) return null;
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        if (type instanceof Class) {
            if (((Class) type).isArray()) return ((Class) type).getComponentType();
            if (Collection.class.isAssignableFrom((Class<?>) type)) {
                return ((Class) type).getTypeParameters()[0].getBounds()[0];
            }
        }
        return type;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<T>) type;
        }
        if (type instanceof ParameterizedType) {
            if (((ParameterizedType) type).getRawType() instanceof Class<?>) {
                return (Class<T>) ((ParameterizedType) type).getRawType();
            }
        }
        if (type instanceof TypeVariable<?>) {
            TypeVariable<?> variable = (TypeVariable<?>) type;
            Type[] bounds = variable.getBounds();
            return getBound(bounds);
        }
        if (type instanceof WildcardType) {
            WildcardType wildcard = (WildcardType) type;
            return getBound(wildcard.getUpperBounds());
        }
        if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type;
            Class<?> rawType = getRawType(genericArrayType.getGenericComponentType());
            if (rawType != null) {
                return (Class<T>) Array.newInstance(rawType, 0).getClass();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> getBound(Type[] bounds) {
        if (bounds.length == 0) {
            return (Class<T>) Object.class;
        } else {
            return getRawType(bounds[0]);
        }
    }

    public static List<Method> getAllMethods(Class<?> clazz) {
        BiFunction<Method, List<Method>, Boolean> filter = (method, methods) -> {
            // we have no interest in generics bridge methods
            if (method.isBridge()) {
                return false;
            }

            // we do not want finalize()
            if ("finalize".equals(method.getName())) {
                return false;
            }

            // same method...
            if (methods.contains(method)) {
                return false;
            }

            // check if a method with the same signature is already available
            for (Method currentMethod : methods) {
                if (Reflection.hasSameSignature(currentMethod, method)) {
                    return false;
                }
            }

            return true;
        };
        return getAllMethods(clazz, filter);
    }

    public static List<Method> getAllMethods(Class<?> clazz, BiFunction<Method, List<Method>, Boolean> filter) {
        ArrayList<Method> methods = new ArrayList<>();

        Consumer<Class<?>> collectMethods = c -> {
            for (Method method : c.getDeclaredMethods()) {
                if (filter.apply(method, methods)) {
                    methods.add(method);
                }
            }
        };

        // collect methods from current class
        collectMethods.accept(clazz);
        for (Class<?> i : clazz.getInterfaces()) {
            collectMethods.accept(i);
        }

        // collect methods from abstract super classes...
        Class currentSuperClass = clazz.getSuperclass();
        while (currentSuperClass != null && currentSuperClass != Object.class) {
            if (Modifier.isAbstract(currentSuperClass.getModifiers())) {
                collectMethods.accept(currentSuperClass);
                for (Class<?> i : currentSuperClass.getInterfaces()) {
                    collectMethods.accept(i);
                }
            }
            currentSuperClass = currentSuperClass.getSuperclass();
        }

        // sort out somewhere implemented abstract methods
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            Iterator<Method> methodIterator = methods.iterator();
            while (methodIterator.hasNext()) {
                Method method = methodIterator.next();
                if (Modifier.isAbstract(method.getModifiers())) {
                    try {
                        Method foundMethod = currentClass.getMethod(method.getName(), method.getParameterTypes());
                        // if method is implemented in the current class -> remove it
                        if (foundMethod != null && !Modifier.isAbstract(foundMethod.getModifiers())) {
                            methodIterator.remove();
                        }
                    } catch (Exception ignore) { }
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return methods;
    }

    public static boolean hasSameSignature(Method a, Method b) {
        return a.getName().equals(b.getName())
                && a.getReturnType().equals(b.getReturnType())
                && Arrays.equals(a.getParameterTypes(), b.getParameterTypes());
    }
}
