package com.alice.emily.module.okhttp;

import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ MetricRegistry.class, AbstractEndpoint.class })
public class OkHttpMetricsConfiguration {

    private final ObjectProvider<MetricRegistry> metricRegistry;

    public OkHttpMetricsConfiguration(ObjectProvider<MetricRegistry> metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Bean
    public OkHttpEndpoint okHttpEndpoint() {
        MetricRegistry registry = metricRegistry.getIfAvailable();
        return registry == null ? null : new OkHttpEndpoint(registry);
    }

    @Bean
    @OkHttpInterceptor
    public OkHttpMetricsInterceptor okHttpMetricsInterceptor() {
        MetricRegistry registry = metricRegistry.getIfAvailable();
        return registry == null ? null : new OkHttpMetricsInterceptor(registry);
    }
}