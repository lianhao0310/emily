package com.alice.emily.module.okhttp;

import com.alice.emily.core.Configurer;
import okhttp3.OkHttpClient;

/**
 * Created by lianhao on 2017/3/30.
 */
@FunctionalInterface
public interface OkHttpConfigurer extends Configurer<OkHttpClient.Builder> {

    @Override
    void configure(OkHttpClient.Builder builder);
}
