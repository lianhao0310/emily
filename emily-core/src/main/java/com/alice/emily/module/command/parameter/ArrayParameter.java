package com.alice.emily.module.command.parameter;

import com.google.common.base.Joiner;
import jodd.typeconverter.TypeConverterManager;

import java.util.Iterator;

/**
 * Created by lianhao on 2016/11/15.
 */
public class ArrayParameter<T> implements Parameter<T> {

    private final Class<T> type;
    private final String name;

    public ArrayParameter(Class<T> type, String name) {
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
        String array = Joiner.on(",").join(iterator);
        return TypeConverterManager.convertType(array, type);
    }
}
