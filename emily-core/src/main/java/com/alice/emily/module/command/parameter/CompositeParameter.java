package com.alice.emily.module.command.parameter;

import com.alice.emily.exception.EmilyException;
import com.alice.emily.utils.Reflection;
import jodd.typeconverter.TypeConverterManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by liupin on 2016/11/15.
 */
public class CompositeParameter implements Parameter<Object[]> {

    private final List<Parameter<?>> parameters = new ArrayList<>();

    public CompositeParameter(Method method) {
        for (java.lang.reflect.Parameter p : method.getParameters()) {
            Parameter<?> parameter = getParameter(p.getParameterizedType(), p.getName());
            parameters.add(parameter);
        }
    }

    public CompositeParameter(Field[] fields) {
        for (Field field : fields) {
            Parameter<?> parameter = getParameter(field.getType(), field.getName());
            parameters.add(parameter);
        }
    }

    @SuppressWarnings("unchecked")
    private Parameter<?> getParameter(Type type, String name) {
        if (support(type)) {
            if (Reflection.isArray(type)) {
                return new ArrayParameter((Class) type, name);
            } else if (Reflection.isCollection(type)) {
                return new CollectionParameter(type, name);
            } else {
                return new SingleParameter((Class<?>) type, name);
            }
        } else {
            if (type instanceof Class<?>) {
                Field[] fields = ((Class<?>) type).getDeclaredFields();
                Optional<Type> any = Arrays.stream(fields)
                        .map(Field::getGenericType)
                        .filter(t -> !support(t)).findAny();
                if (any.isPresent()) {
                    throw new EmilyException("Not supported type: " + type);
                }
                return new BeanParameter((Class<?>) type, name);
            } else {
                throw new EmilyException("Not supported type: " + type);
            }
        }
    }

    private boolean support(Type type) {
        Class<?> rawType = Reflection.getRawType(type);
        Type elementType = Reflection.getElementType(type);
        boolean supportRawType = TypeConverterManager.lookup(rawType) != null || Collection.class.isAssignableFrom(rawType);
        boolean supportElementType = elementType != null && TypeConverterManager.lookup(Reflection.getRawType(elementType)) != null;
        return supportRawType && supportElementType;
    }

    @Override
    public String name() {
        if (parameters.isEmpty()) return "";
        String children = parameters.stream().map(Parameter::name).collect(Collectors.joining(", "));
        return "( " + children + " )";
    }

    @Override
    public Object[] convert(Iterator<String> iterator) {
        if (!iterator.hasNext()) return new Object[parameters.size()];
        return parameters.stream().map(converter -> converter.convert(iterator)).toArray();
    }
}
