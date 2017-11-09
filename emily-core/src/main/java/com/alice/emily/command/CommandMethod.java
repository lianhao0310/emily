package com.alice.emily.command;

import com.alice.emily.utils.LOG;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by liupin on 2016/11/10.
 */
@Getter
public class CommandMethod<T, X> {

    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private final Object bean;
    private final String category;
    private final String action;
    private final Method method;
    private final MethodParameter[] parameters;
    private final boolean isStatic;

    private volatile String description;

    @Setter
    private ConversionService conversionService;

    public CommandMethod(Object bean, Method method,
                         String category, String action) {
        this.bean = bean;
        this.category = category;
        this.action = action;
        this.method = method;
        this.method.setAccessible(true);
        this.parameters = methodParameters(method);
        this.isStatic = Modifier.isStatic(method.getModifiers());
    }

    private CommandMethod(Object bean, String category, String action, Method method,
                          MethodParameter[] parameters, boolean isStatic,
                          ConversionService conversionService) {
        this.bean = bean;
        this.category = category;
        this.action = action;
        this.method = method;
        this.parameters = parameters;
        this.isStatic = isStatic;
        this.conversionService = conversionService;
    }

    public CommandMethod<T, X> clone(String action) {
        return new CommandMethod<>(bean, category, action, method, parameters, isStatic, conversionService);
    }

    public T invoke(String... args) {
        if (ArrayUtils.isEmpty(parameters)) {
            return invoke(new Object[0]);
        }
        Object[] resolvedArgs = new Object[parameters.length];
        if (args != null) {
            for (int i = 0; i < parameters.length; i++) {
                Class<?> parameterType = parameters[i].getParameterType();
                if (i < args.length) {
                    // the last vararg or collection
                    if (i == parameters.length - 1
                            && (parameters[i].getParameter().isVarArgs()
                            || Collection.class.isAssignableFrom(parameterType))) {
                        String[] subArgs = ArrayUtils.subarray(args, i, args.length);
                        resolvedArgs[i] = convert(parameterType, subArgs);
                        break;
                    }
                    resolvedArgs[i] = convert(parameterType, args[i]);
                } else {
                    if (parameterType.isPrimitive()) {
                        LOG.CMD.error("Command method parameter is primitive, must be provided: {}", parameters[i]);
                        return null;
                    } else {
                        resolvedArgs[i] = null;
                    }
                }

            }
        }
        return invoke(resolvedArgs);
    }

    @SuppressWarnings("unchecked")
    public T invoke(Object... args) {
        Object result = null;
        try {
            if (isStatic) {
                result = method.invoke(null, args);
            } else {
                result = method.invoke(bean, args);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return (T) result;
    }

    private Object convert(Class<?> parameterType, Object arg) {
        TypeDescriptor sourceType = TypeDescriptor.valueOf(arg.getClass());
        TypeDescriptor targetType = TypeDescriptor.valueOf(parameterType);
        return conversionService.convert(arg, sourceType, targetType);
    }

    private static MethodParameter[] methodParameters(Method method) {
        int parameterCount = method.getParameterCount();
        MethodParameter[] methodParameters = new MethodParameter[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            methodParameters[i] = new MethodParameter(method, i);
            methodParameters[i].initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
        }
        return methodParameters;
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
        if (description == null) {
            String param = "";
            if (ArrayUtils.isNotEmpty(parameters)) {
                param = Arrays.stream(parameters)
                        .map(MethodParameter::getParameterName)
                        .collect(Collectors.joining(", ", "( ", " )"));
            }
            description = String.format("%-10s%-20s %s", category, action, param);
        }
        return description;
    }
}
