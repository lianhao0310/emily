package com.alice.emily.module.retrofit.auth;

import com.alice.emily.core.Configurer;
import okhttp3.OkHttpClient;

/**
 * Created by liupin on 2017/4/1.
 */
@FunctionalInterface
public interface AuthenticationConfigurer extends Configurer<OkHttpClient.Builder> {

    void configure(OkHttpClient.Builder builder);
}
