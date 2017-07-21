package com.alice.emily.autoconfigure;

import com.alice.emily.utils.LOG;
import com.alice.emily.utils.Threads;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

/**
 * Created by liupin on 2017/2/16.
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
@EnableRetry(proxyTargetClass = true)
@EnableScheduling
public class NiceFeatureAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ThreadPoolTaskExecutor.class)
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(Threads.processors());
        executor.setKeepAliveSeconds(60);
        executor.setQueueCapacity(1024);
        executor.setThreadNamePrefix("EUP-EXE-");
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(AsyncConfigurer.class)
    public AsyncConfigurer asyncConfigurer(ThreadPoolTaskExecutor executor) {
        return new AsyncConfigurerSupport() {
            @Override
            public Executor getAsyncExecutor() {
                return executor;
            }

            @Override
            public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                return (ex, method, params) ->
                        LOG.THREAD.error("Error happened when execute {}({}): ", method, params, ex);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(ThreadPoolTaskScheduler.class)
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setDaemon(true);
        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.setThreadNamePrefix("EUP-TIMER-");
        return scheduler;
    }

    @Bean
    @ConditionalOnMissingBean(SchedulingConfigurer.class)
    public SchedulingConfigurer schedulingConfigurer(ThreadPoolTaskScheduler scheduler) {
        return taskRegistrar -> taskRegistrar.setTaskScheduler(scheduler);
    }
}
