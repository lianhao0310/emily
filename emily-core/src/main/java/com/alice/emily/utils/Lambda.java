package com.alice.emily.utils;

import java.util.function.*;

/**
 * Created by lianhao on 2016/11/17.
 */
public class Lambda {

    private Lambda() { }

    public static Runnable wrap(Throwing.Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                throw Errors.rethrow(e);
            }
        };
    }

    public static <T> Supplier<T> wrap(Throwing.Supplier<T> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable e) {
                throw Errors.rethrow(e);
            }
        };
    }

    public static <T> Consumer<T> wrap(Throwing.Consumer<T> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Throwable e) {
                throw Errors.rethrow(e);
            }
        };
    }

    public static <T, R> Function<T, R> wrap(Throwing.Function<T, R> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Throwable e) {
                throw Errors.rethrow(e);
            }
        };
    }

    public static <T> Predicate<T> wrap(Throwing.Predicate<T> predicate) {
        return t -> {
            try {
                return predicate.test(t);
            } catch (Throwable e) {
                throw Errors.rethrow(e);
            }
        };
    }

    public static <T, U> BiConsumer<T, U> wrap(Throwing.BiConsumer<T, U> biConsumer) {
        return (t, u) -> {
            try {
                biConsumer.accept(t, u);
            } catch (Throwable e) {
                throw Errors.rethrow(e);
            }
        };
    }

    public static <T, U, R> BiFunction<T, U, R> wrap(Throwing.BiFunction<T, U, R> biFunction) {
        return (t, u) -> {
            try {
                return biFunction.apply(t, u);
            } catch (Throwable e) {
                throw Errors.rethrow(e);
            }
        };
    }

    public static <T, U> BiPredicate<T, U> wrap(Throwing.BiPredicate<T, U> biPredicate) {
        return (t, u) -> {
            try {
                return biPredicate.test(t, u);
            } catch (Throwable e) {
                throw Errors.rethrow(e);
            }
        };
    }

    public interface Throwing {
        interface Specific {
            @FunctionalInterface
            interface Runnable<E extends Throwable> {
                void run() throws E;
            }

            @FunctionalInterface
            interface Supplier<T, E extends Throwable> {
                T get() throws E;
            }

            @FunctionalInterface
            interface Consumer<T, E extends Throwable> {
                void accept(T t) throws E;
            }

            @FunctionalInterface
            interface Function<T, R, E extends Throwable> {
                R apply(T t) throws E;
            }

            @FunctionalInterface
            interface Predicate<T, E extends Throwable> {
                boolean test(T t) throws E;
            }

            @FunctionalInterface
            interface BiConsumer<T, U, E extends Throwable> {
                void accept(T t, U u) throws E;
            }

            @FunctionalInterface
            interface BiFunction<T, U, R, E extends Throwable> {
                R apply(T t, U u) throws E;
            }

            @FunctionalInterface
            interface BiPredicate<T, U, E extends Throwable> {
                boolean test(T t, U u) throws E;
            }
        }

        @FunctionalInterface
        interface Runnable extends Specific.Runnable<Throwable> {

        }

        @FunctionalInterface
        interface Supplier<T> extends Specific.Supplier<T, Throwable> {

        }

        @FunctionalInterface
        interface Consumer<T> extends Specific.Consumer<T, Throwable> {

        }

        @FunctionalInterface
        interface Function<T, R> extends Specific.Function<T, R, Throwable> {

        }

        @FunctionalInterface
        interface Predicate<T> extends Specific.Predicate<T, Throwable> {

        }

        @FunctionalInterface
        interface BiConsumer<T, U> extends Specific.BiConsumer<T, U, Throwable> {

        }

        @FunctionalInterface
        interface BiFunction<T, U, R> extends Specific.BiFunction<T, U, R, Throwable> {

        }

        @FunctionalInterface
        interface BiPredicate<T, U> extends Specific.BiPredicate<T, U, Throwable> {

        }
    }
}
