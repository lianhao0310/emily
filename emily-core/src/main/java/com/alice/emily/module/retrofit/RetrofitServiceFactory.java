package com.alice.emily.module.retrofit;

import retrofit2.Retrofit;

/**
 * Created by liupin on 2017/3/31.
 */
public interface RetrofitServiceFactory {
    <T> T createServiceInstance(Class<T> serviceClass, String client);

    Retrofit getRetrofit(String client);
}
