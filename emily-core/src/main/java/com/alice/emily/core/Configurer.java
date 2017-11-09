package com.alice.emily.core;

@FunctionalInterface
public interface Configurer<T> {

    void configure(T object) throws Exception;
}