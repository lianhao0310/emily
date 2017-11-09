package com.alice.emily.autoconfigure.core;

import com.alice.emily.utils.Threads;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

/**
 * Created by lianhao on 2017/2/16.
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableAsync(proxyTargetClass = true)
@EnableRetry(proxyTargetClass = true)
@EnableScheduling
public class NiceFeatureAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ThreadPoolTaskExecutor.class)
    @ConfigurationProperties(prefix = "emily.executor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(Threads.processors() * 2);
        executor.setKeepAliveSeconds(60);
        executor.setQueueCapacity(1024 * 4);
        executor.setThreadNamePrefix("executor-");
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(AsyncConfigurer.class)
    public AsyncConfigurer asyncConfigurer(ThreadPoolTaskExecutor executor) {
        return new AsyncConfigurer() {
            @Override
            public Executor getAsyncExecutor() {
                return executor;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(ThreadPoolTaskScheduler.class)
    @ConfigurationProperties(prefix = "emily.scheduler")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setDaemon(true);
        scheduler.setPoolSize(1);
        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.setThreadNamePrefix("scheduler-");
        return scheduler;
    }

    @Bean
    @ConditionalOnMissingBean(SchedulingConfigurer.class)
    public SchedulingConfigurer schedulingConfigurer(ThreadPoolTaskScheduler scheduler) {
        return taskRegistrar -> taskRegistrar.setTaskScheduler(scheduler);
    }
}
