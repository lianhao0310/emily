package com.alice.emily.module.okhttp;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnClass({ RestTemplateCustomizer.class, RestTemplate.class })
public class OkHttpRestTemplateConfiguration {

    private final ObjectProvider<OkHttpClient> okHttpClient;

    public OkHttpRestTemplateConfiguration(ObjectProvider<OkHttpClient> okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Bean
    @Order(2)
    @ConditionalOnMissingBean(OkHttp3ClientHttpRequestFactory.class)
    public OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory() {
        OkHttpClient httpClient = okHttpClient.getIfAvailable();
        return httpClient == null ? null : new OkHttp3ClientHttpRequestFactory(httpClient);
    }

    @Bean
    @ConditionalOnBean(OkHttp3ClientHttpRequestFactory.class)
    public RestTemplateCustomizer okHttp3RestTemplateCustomizer(OkHttp3ClientHttpRequestFactory requestFactory) {
        return restTemplate -> restTemplate.setRequestFactory(requestFactory);
    }
}