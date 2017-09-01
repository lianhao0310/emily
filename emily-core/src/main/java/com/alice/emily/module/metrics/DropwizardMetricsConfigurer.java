package com.alice.emily.module.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

import java.lang.management.ManagementFactory;

public class DropwizardMetricsConfigurer extends MetricsConfigurerAdapter {

    private final MetricRegistry metricRegistry;
    private final HealthCheckRegistry healthCheckRegistry;

    public DropwizardMetricsConfigurer() {
        this.metricRegistry = new MetricRegistry();
        this.healthCheckRegistry = new HealthCheckRegistry();

        this.metricRegistry.register("jvm.memory", new MemoryUsageGaugeSet());
        this.metricRegistry.register("jvm.garbage", new GarbageCollectorMetricSet());
        this.metricRegistry.register("jvm.threads", new ThreadStatesGaugeSet());
        this.metricRegistry.register("jvm.files", new FileDescriptorRatioGauge());
        this.metricRegistry.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
    }

    @Override
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    @Override
    public HealthCheckRegistry getHealthCheckRegistry() {
        return healthCheckRegistry;
    }

}