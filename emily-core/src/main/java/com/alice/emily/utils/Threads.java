package com.alice.emily.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.experimental.UtilityClass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by liupin on 2016/12/9.
 */
@UtilityClass
public class Threads {

    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    public static int processors() {
        return AVAILABLE_PROCESSORS;
    }

    public static ThreadFactory newThreadFactory(String name) {
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder();
        builder.setDaemon(true);
        builder.setUncaughtExceptionHandler((t, e) -> LOG.THREAD.error("Caught an Exception in {}", t, e));
        builder.setNameFormat("%d-" + name);
        return builder.build();
    }

    public static ExecutorService newSingle(String name) {
        ThreadFactory factory = newThreadFactory(name);
        return Executors.newSingleThreadExecutor(factory);
    }

    public static ExecutorService newCached(String name) {
        return newExecutor(-1, name);
    }

    public static ExecutorService newExecutor(String name) {
        return newExecutor(1, name);
    }

    public static ExecutorService newExecutor(int times, String name) {
        ThreadFactory factory = newThreadFactory(name);
        if (times < 0) {
            return Executors.newCachedThreadPool(factory);
        }
        times = Math.max(1, times);
        return Executors.newFixedThreadPool(processors() * times, factory);
    }

    public static ScheduledExecutorService newScheduler(String name) {
        return newScheduler(1, name);
    }

    public static ScheduledExecutorService newScheduler(int times, String name) {
        ThreadFactory factory = newThreadFactory(name);
        times = Math.max(1, times);
        return Executors.newScheduledThreadPool(processors() * times, factory);
    }

    public static void shutdown(ExecutorService... executors) {
        if (executors == null || executors.length <= 0) return;

        for (ExecutorService executor : executors) {
            if (executor != null && !executor.isTerminated()) {
                executor.shutdown();
            }
        }

        for (ExecutorService executor : executors) {
            if (executor != null && !executor.isTerminated()) {
                try {
                    executor.awaitTermination(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    LOG.THREAD.error("Cannot shutdown executor {} in 5s, executor will be force shutdown", executor, e);
                } finally {
                    executor.shutdownNow();
                }
            }
        }
    }
}
