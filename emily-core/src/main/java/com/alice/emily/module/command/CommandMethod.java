package com.alice.emily.module.command;

import com.alice.emily.module.command.parameter.CompositeParameter;
import com.alice.emily.module.command.parameter.Parameter;
import com.alice.emily.utils.Reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by lianhao on 2016/11/10.
 */
public class CommandMethod<T, X> {

    private final Object bean;
    private final String category;
    private final String action;
    private final Method method;
    private final Parameter<Object[]> converter;
    private final boolean isStatic;

    public CommandMethod(Object bean, Method method, String category, String action) {
        this.bean = bean;
        this.category = category;
        this.action = action;
        this.method = method;
        this.method.setAccessible(true);
        this.isStatic = Modifier.isStatic(method.getModifiers());
        this.converter = new CompositeParameter(method);
    }

    private CommandMethod(Object bean, String category, String action, Method method,
                          Parameter<Object[]> converter, boolean isStatic) {
        this.bean = bean;
        this.category = category;
        this.action = action;
        this.method = method;
        this.converter = converter;
        this.isStatic = isStatic;
    }

    public CommandMethod<T, X> clone(String action) {
        return new CommandMethod<>(bean, category, action, method, converter, isStatic);
    }

    public Object getBean() {
        return bean;
    }

    public String getCategory() {
        return category;
    }

    public String getAction() {
        return action;
    }

    public Method getMethod() {
        return method;
    }

    public T invoke(String... args) throws InvocationTargetException, IllegalAccessException {
        return invoke(converter.convert(args != null ? Arrays.asList(args).iterator() : Collections.emptyIterator()));
    }

    public T invoke(Object... args) throws InvocationTargetException, IllegalAccessException {
        Object result;
        if (isStatic) {
            result = method.invoke(null, args);
        } else {
            result = method.invoke(bean, args);
        }
        return Reflection.cast(result);
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        CommandMethod<?, ?> that = (CommandMethod<?, ?>) object;

        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        return method != null ? method.equals(that.method) : that.method == null;
    }

    @Override
    public int hashCode() {
        int result = category != null ? category.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%-10s%-20s %s", category, action, converter.name());
    }
}
