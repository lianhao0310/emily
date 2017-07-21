package com.alice.emily.module.resteasy;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.plugins.spring.ResteasyClientInitializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Created by liupin on 2017/4/6.
 */
@Configuration
@ConditionalOnClass(ResteasyClient.class)
public class ResteasyClientConfiguration {

    private final ObjectProvider<Collection<ResteasyClientConfigurer>> resteasyClientConfigurers;

    public ResteasyClientConfiguration(ObjectProvider<Collection<ResteasyClientConfigurer>> resteasyClientConfigurers) {
        this.resteasyClientConfigurers = resteasyClientConfigurers;
        new ResteasyClientInitializer();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("emily.resteasy.client")
    public ResteasyClientBuilder resteasyClientBuilder() {
        return new ResteasyClientBuilder().connectionPoolSize(10);
    }

    @Bean
    @ConditionalOnMissingBean
    public ResteasyClient resteasyClient(ResteasyClientBuilder resteasyClientBuilder) {
        Collection<ResteasyClientConfigurer> configurers = resteasyClientConfigurers.getIfAvailable();
        if (configurers != null && !configurers.isEmpty()) {
            for (ResteasyClientConfigurer configurer : configurers) {
                configurer.configure(resteasyClientBuilder);
            }
        }
        return resteasyClientBuilder.build();
    }
}
