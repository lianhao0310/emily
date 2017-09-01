package com.alice.emily.utils;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

public class Lazy<T> extends LazyInitializer<T> {

    private final Lambda.Throwing.Supplier<T> supplier;

    private Lazy(@NonNull Lambda.Throwing.Supplier<T> supplier) {
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

    public static <V> Lazy<V> of(@NonNull Lambda.Throwing.Supplier<V> supplier) {
        return new Lazy<>(supplier);
    }
}
