package com.alice.emily.validator;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * Created by lianhao on 2017/6/21.
 */
@FunctionalInterface
public interface ValidateFunc<T> extends Function<T, Boolean> {

    static <E extends Comparable<E>> ValidateFunc<E> greeterThan(@Nonnull E e) {
        return o -> o.compareTo(e) > 0;
    }

    static <E extends Comparable<E>> ValidateFunc<E> lessThan(@Nonnull E e) {
        return o -> o.compareTo(e) < 0;
    }
}
