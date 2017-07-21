package com.alice.emily.autoconfigure;

import com.codahale.metrics.MetricRegistry;
import com.alice.emily.module.metrics.DropwizardMetricsConfigurer;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurer;
import org.springframework.boot.actuate.autoconfigure.MetricsDropwizardAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lianhao on 2017/3/27.
 */
@Configuration
@ConditionalOnClass({ MetricRegistry.class, MetricsConfigurer.class })
@EnableMetrics(proxyTargetClass = true)
@AutoConfigureBefore(MetricsDropwizardAutoConfiguration.class)
public class MetricsAutoConfiguration {

    @Bean
    public static DropwizardMetricsConfigurer dropwizardMetricsConfigurer() {
        return new DropwizardMetricsConfigurer();
    }

}
