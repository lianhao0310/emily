package com.alice.emily.module.retrofit;

import com.google.common.collect.Maps;
import com.alice.emily.core.BeanProvider;
import com.alice.emily.module.retrofit.auth.AuthenticationConfigurer;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Factory for constructing {@link Retrofit} service instances.
 *
 * @author liupin
 */
@Getter
public class RetrofitServiceFactoryImpl implements RetrofitServiceFactory, DisposableBean {

    private final OkHttpClient okHttpClient;
    private final RetrofitProperties properties;
    private final ConcurrentMap<String, Retrofit> retrofits = Maps.newConcurrentMap();

    public RetrofitServiceFactoryImpl(OkHttpClient okHttpClient, RetrofitProperties properties) {
        this.okHttpClient = okHttpClient;
        this.properties = properties;
    }

    @Override
    public <T> T createServiceInstance(Class<T> serviceClass, String client) {
        Retrofit retrofit = retrofits.computeIfAbsent(client, this::getRetrofit);
        if (retrofit == null) {
            throw new IllegalStateException("Cannot build Retrofit for client " + client
                    + ", please check your application configuration!!!");
        }
        return retrofit.create(serviceClass);
    }

    @Override
    public Retrofit getRetrofit(String client) {
        RetrofitProperties.RetrofitClient retrofitClient = properties.getClients().get(client);
        Retrofit retrofit = null;
        if (retrofitClient != null) {
            // check if Retrofit was provided with specified Qualifier
            retrofit = BeanProvider.getBean(Retrofit.class, client);

            // if not provided, assemble retrofit from configurations
            if (retrofit == null) {
                retrofit = assembleRetrofit(retrofitClient);
            }
        }
        return retrofit;
    }

    private Retrofit assembleRetrofit(RetrofitProperties.RetrofitClient retrofitClient) {
        // prepare components
        List<CallAdapter.Factory> adapters = BeanProvider.getBeans(CallAdapter.Factory.class);
        List<Converter.Factory> converters = BeanProvider.getBeans(Converter.Factory.class);
        AnnotationAwareOrderComparator.sort(converters);
        OkHttpClient httpClient = assembleOkHttpClient(retrofitClient);

        // ensure baseUrl end with /
        String baseUrl = retrofitClient.getBaseUrl();
        String url = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";

        // build retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .callFactory(httpClient)
                .validateEagerly(true);
        adapters.forEach(builder::addCallAdapterFactory);
        converters.forEach(builder::addConverterFactory);
        return builder.build();
    }

    private OkHttpClient assembleOkHttpClient(RetrofitProperties.RetrofitClient client) {
        OkHttpClient httpClient = this.okHttpClient;

        // prepare authentication
        AuthenticationConfigurer configurer = null;
        if (client.getBasic() != null) {
            configurer = client.getBasic();
        }
        if (client.getDigest() != null) {
            configurer = client.getDigest();
        }

        // configure okHttpClient
        if (configurer != null) {
            OkHttpClient.Builder builder = httpClient.newBuilder();
            configurer.configure(builder);
            httpClient = builder.build();
        }
        return httpClient;
    }

    @Override
    public void destroy() throws Exception {

    }
}
