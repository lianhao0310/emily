package com.alice.emily.module.command.parameter;

import jodd.typeconverter.TypeConverterManager;

import java.util.Iterator;

/**
 * Created by liupin on 2016/11/15.
 */
public class SingleParameter<T> implements Parameter<T> {

    private final Class<T> type;
    private final String name;

    public SingleParameter(Class<T> type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public T convert(Iterator<String> iterator) {
        return iterator.hasNext() ? TypeConverterManager.convertType(iterator.next(), type) : null;
    }
}
