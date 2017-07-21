package com.alice.emily.module.retrofit.auth;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lianhao on 2017/4/1.
 */
public class BasicAuthenticationConfigurer extends CredentialAuthenticationConfigurer {

    @Override
    public void configure(OkHttpClient.Builder builder) {
        builder.addInterceptor(chain -> {
            String authToken = Credentials.basic(username, password);
            Request request = chain.request()
                    .newBuilder()
                    .header("Authorization", authToken)
                    .build();
            return chain.proceed(request);
        });
    }
}
