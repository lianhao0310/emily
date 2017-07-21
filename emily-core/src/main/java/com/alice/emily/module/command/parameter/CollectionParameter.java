package com.alice.emily.module.command.parameter;

import com.google.common.collect.Iterators;
import com.alice.emily.utils.Reflection;
import jodd.typeconverter.TypeConverterManager;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by liupin on 2016/11/15.
 */
public class CollectionParameter<T> implements Parameter<T> {

    private final Type type;
    private final String name;

    public CollectionParameter(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public T convert(Iterator<String> iterator) {
        if (!iterator.hasNext()) return null;
        String[] strings = Iterators.toArray(iterator, String.class);
        Class<? extends Collection> rawType = Reflection.getRawType(type);
        Class<?> elementType = Reflection.cast(Reflection.getElementType(type));
        Collection<?> collection = TypeConverterManager.convertToCollection(strings, rawType, elementType);
        return Reflection.cast(collection);
    }
}
