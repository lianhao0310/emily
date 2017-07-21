package com.alice.emily.module.command.parameter;

import java.util.Iterator;

/**
 * Created by lianhao on 2016/11/15.
 */
public interface Parameter<T> {

    String name();

    T convert(Iterator<String> iterator);
}
