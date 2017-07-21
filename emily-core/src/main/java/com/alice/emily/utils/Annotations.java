package com.alice.emily.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by liupin on 2017/1/2.
 */
public class Annotations {

    public static <T extends Annotation> boolean isAnnotationPresent(Method method, Class<T> annotation) {
        if (method.isAnnotationPresent(annotation)) return true;
        Class<?> clazz = method.getDeclaringClass();
        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null && superClass != Object.class) {
            try {
                Method m = superClass.getMethod(method.getName(), method.getParameterTypes());
                if (m.isAnnotationPresent(annotation)) return true;
            } catch (NoSuchMethodException ignore) { }
            superClass = clazz.getSuperclass();
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> i : interfaces) {
            try {
                Method m = i.getMethod(method.getName(), method.getParameterTypes());
                if (m.isAnnotationPresent(annotation)) return true;
            } catch (NoSuchMethodException ignore) { }
        }

        return false;
    }

    @SuppressWarnings("Duplicates")
    public static <T extends Annotation> T getAnnotation(Method method, Class<T> annotation) {
        T a = method.getAnnotation(annotation);
        if (a != null) return a;
        Class<?> clazz = method.getDeclaringClass();
        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null && superClass != Object.class) {
            try {
                Method m = superClass.getMethod(method.getName(), method.getParameterTypes());
                a = m.getAnnotation(annotation);
                if (a != null) return a;
            } catch (NoSuchMethodException ignore) { }
            superClass = clazz.getSuperclass();
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> i : interfaces) {
            try {
                Method m = i.getMethod(method.getName(), method.getParameterTypes());
                a = m.getAnnotation(annotation);
                if (a != null) return a;
            } catch (NoSuchMethodException ignore) { }
        }
        return null;
    }

    public static boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> annotation) {
        Class<?> superClass = clazz;
        while (superClass != null && superClass != Object.class) {
            if (superClass.isAnnotationPresent(annotation)) return true;
            superClass = superClass.getSuperclass();
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> i : interfaces) {
            if (i.isAnnotationPresent(annotation)) return true;
        }
        return false;
    }

    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotation) {
        Class<?> superClass = clazz;
        while (superClass != null && superClass != Object.class) {
            T a = superClass.getAnnotation(annotation);
            if (a != null) return a;
            superClass = superClass.getSuperclass();
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> i : interfaces) {
            T a = i.getAnnotation(annotation);
            if (a != null) return a;
        }
        return null;
    }

    public static <T extends Annotation> Class<?> getDeclaringClassWithAnnotationPresent(Class<?> type, Class<T> annotation) {
        Class<?> declaringClass = type;
        while (declaringClass != null) {
            if (isAnnotationPresent(declaringClass, annotation)) {
                return declaringClass;
            }
            declaringClass = declaringClass.getDeclaringClass();
        }
        return null;
    }
}
