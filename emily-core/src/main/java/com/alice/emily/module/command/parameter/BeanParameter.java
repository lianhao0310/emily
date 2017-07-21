package com.alice.emily.module.command.parameter;

import com.alice.emily.exception.EmilyException;
import com.alice.emily.utils.Reflection;

import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * Created by liupin on 2016/11/15.
 */
public class BeanParameter<T> implements Parameter<T> {

    private final Class<T> type;
    private final Field[] fields;
    private final String name;
    private CompositeParameter delegate;

    public BeanParameter(Class<T> type, String name) {
        this.type = type;
        this.fields = type.getDeclaredFields();
        this.name = name;
        this.delegate = new CompositeParameter(fields);
        for (Field field : fields) {
            field.setAccessible(true);
        }
    }

    @Override
    public String name() {
        return name + delegate.name();
    }

    @Override
    public T convert(Iterator<String> iterator) {
        if (!iterator.hasNext()) return null;
        Object instance = Reflection.newInstance(type);
        Object[] objects = delegate.convert(iterator);
        for (int i = 0; i < fields.length; i++) {
            try {
                fields[i].set(instance, objects[i]);
            } catch (IllegalAccessException e) {
                throw new EmilyException(e);
            }
        }
        return Reflection.cast(instance);
    }
}
