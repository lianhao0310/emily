package com.alice.emily.module.okhttp;

import okhttp3.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by liupin on 2017/4/10.
 */
@Configuration
@Import({ OkHttpLoggingConfiguration.class, OkHttpMetricsConfiguration.class })
public class OkHttpClientConfiguration {

    private final OkHttpProperties properties;
    private final ObjectProvider<Collection<OkHttpConfigurer>> okHttpConfigurers;
    private final ObjectProvider<Collection<Interceptor>> okHttpInterceptors;
    private final ObjectProvider<Collection<Interceptor>> okHttpNetworkInterceptors;
    private final ObjectProvider<CookieJar> cookieJarProvider;
    private final ObjectProvider<Dns> dnsProvider;

    public OkHttpClientConfiguration(OkHttpProperties properties,
                                   ObjectProvider<Collection<OkHttpConfigurer>> okHttpConfigurers,
                                   @OkHttpInterceptor ObjectProvider<Collection<Interceptor>> okHttpInterceptors,
                                   @OkHttpNetworkInterceptor ObjectProvider<Collection<Interceptor>> okHttpNetworkInterceptors,
                                   ObjectProvider<CookieJar> cookieJarProvider,
                                   ObjectProvider<Dns> dnsProvider) {
        this.properties = properties;
        this.okHttpConfigurers = okHttpConfigurers;
        this.okHttpInterceptors = okHttpInterceptors;
        this.okHttpNetworkInterceptors = okHttpNetworkInterceptors;
        this.cookieJarProvider = cookieJarProvider;
        this.dnsProvider = dnsProvider;
    }

    @Bean
    @ConditionalOnMissingBean
    public OkHttpClient okHttpClient() throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // timeout related configuration
        builder.connectTimeout(properties.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        builder.readTimeout(properties.getReadTimeout(), TimeUnit.MILLISECONDS);
        builder.writeTimeout(properties.getWriteTimeout(), TimeUnit.MILLISECONDS);
        builder.pingInterval(properties.getPingInterval(), TimeUnit.MILLISECONDS);

        // action related configuration
        builder.followRedirects(properties.isFollowRedirects());
        builder.followSslRedirects(properties.isFollowSslRedirects());
        builder.retryOnConnectionFailure(properties.isRetryOnConnectionFailure());

        if (properties.getCache().getMode() != OkHttpProperties.Cache.Mode.NONE) {
            builder.cache(okHttpCache());
        }

        CookieJar cookieJar = cookieJarProvider.getIfAvailable();
        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }

        Dns dns = dnsProvider.getIfAvailable();
        if (dns != null) {
            builder.dns(dns);
        }

        Collection<Interceptor> interceptors = okHttpInterceptors.getIfAvailable();
        if (interceptors != null) {
            interceptors.forEach(builder::addInterceptor);
        }

        Collection<Interceptor> networkInterceptors = okHttpNetworkInterceptors.getIfAvailable();
        if (networkInterceptors != null) {
            networkInterceptors.forEach(builder::addNetworkInterceptor);
        }

        Collection<OkHttpConfigurer> configurers = okHttpConfigurers.getIfAvailable();
        if (configurers != null) {
            for (OkHttpConfigurer configurer : configurers) {
                configurer.configure(builder);
            }
        }
        return builder.build();
    }

    @Lazy
    @Bean
    @ConditionalOnMissingBean
    public Cache okHttpCache() throws IOException {
        File cacheDir;
        String prefix = "okhttp3-cache";
        String directory = properties.getCache().getDirectory();
        switch (properties.getCache().getMode()) {
            case TEMPORARY:
                if (directory != null) {
                    cacheDir = Files.createTempDirectory(new File(directory).getAbsoluteFile().toPath(), prefix).toFile();
                } else {
                    cacheDir = Files.createTempDirectory(prefix).toFile();
                }
                break;
            case PERSISTENT:
                if (directory != null) {
                    cacheDir = new File(directory).getAbsoluteFile();
                } else {
                    cacheDir = new File(prefix).getAbsoluteFile();
                }
                break;
            case NONE:
            default:
                return null;
        }
        return new Cache(cacheDir, properties.getCache().getSize());
    }

}
