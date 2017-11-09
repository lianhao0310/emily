package com.alice.emily.utils;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.jooq.lambda.fi.util.function.CheckedSupplier;

/**
 * Created by lianhao on 2017/6/9.
 */
public class Lazy<T> extends LazyInitializer<T> {

    private final CheckedSupplier<T> supplier;

    private Lazy(@NonNull CheckedSupplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    protected T initialize() throws ConcurrentException {
        try {
            return supplier.get();
        } catch (Throwable throwable) {
            throw new ConcurrentException(throwable);
        }
    }

    @SneakyThrows
    @Override
    public T get() {
        return super.get();
    }

    public static <V> Lazy<V> of(@NonNull CheckedSupplier<V> supplier) {
        return new Lazy<>(supplier);
    }
}
