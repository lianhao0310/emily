package com.alice.emily.module.resteasy;

import com.alice.emily.core.Configurer;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

/**
 * Created by lianhao on 2017/4/6.
 */
@FunctionalInterface
public interface ResteasyClientConfigurer extends Configurer<ResteasyClientBuilder> {

    @Override
    void configure(ResteasyClientBuilder builder);
}