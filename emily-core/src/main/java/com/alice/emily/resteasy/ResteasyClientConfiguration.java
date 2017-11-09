package com.alice.emily.resteasy;

import org.apache.http.client.HttpClient;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by lianhao on 2017/4/6.
 */
@Configuration
@ConditionalOnClass(ResteasyClient.class)
public class ResteasyClientConfiguration {

    private HttpClient httpClient;
    private ResteasyProviderFactory resteasyProviderFactory;
    private final List<ResteasyClientConfigurer> resteasyClientConfigurers;

    public ResteasyClientConfiguration(ObjectProvider<List<ResteasyClientConfigurer>> resteasyClientConfigurers,
                                       ObjectProvider<HttpClient> httpClient,
                                       ObjectProvider<ResteasyProviderFactory> resteasyProviderFactory) {
        this.resteasyClientConfigurers = resteasyClientConfigurers.getIfAvailable();
        this.httpClient = httpClient.getIfAvailable();
        this.resteasyProviderFactory = resteasyProviderFactory.getIfAvailable();
        if (this.resteasyProviderFactory == null) {
            this.resteasyProviderFactory = ResteasyProviderFactory.getInstance();
        }
        RegisterBuiltin.register(this.resteasyProviderFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("emily.resteasy.client")
    public ResteasyClientBuilder resteasyClientBuilder() {
        ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
        if (resteasyProviderFactory != null) {
            clientBuilder.providerFactory(resteasyProviderFactory);
        }
        if (httpClient != null) {
            ClientHttpEngine clientEngine = new ApacheHttpClient4Engine(httpClient);
            clientBuilder.httpEngine(clientEngine);
        }
        return clientBuilder;
    }

    @Bean
    @ConditionalOnMissingBean
    public ResteasyClient resteasyClient(ResteasyClientBuilder resteasyClientBuilder) {
        if (!CollectionUtils.isEmpty(resteasyClientConfigurers)) {
            for (ResteasyClientConfigurer configurer : resteasyClientConfigurers) {
                configurer.configure(resteasyClientBuilder);
            }
        }
        return resteasyClientBuilder.build();
    }
}
