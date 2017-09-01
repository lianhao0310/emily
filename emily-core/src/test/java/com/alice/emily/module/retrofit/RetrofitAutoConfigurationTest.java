package com.alice.emily.module.retrofit;

import com.alice.emily.autoconfigure.JacksonExtAutoConfiguration;
import com.alice.emily.autoconfigure.OkHttpAutoConfiguration;
import com.alice.emily.autoconfigure.ProviderAutoConfiguration;
import com.alice.emily.autoconfigure.RetrofitAutoConfiguration;
import io.reactivex.Observable;
import lombok.Data;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link RetrofitAutoConfiguration}
 *
 * @author lianhao
 */
public class RetrofitAutoConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @Data
    private static class Hello {
        private String message;
    }

    public interface MyService {
        @GET("/hello")
        Call<Hello> sayHello();

        @GET("/hello-observable-scalar")
        Observable<String> toHelloObservable();
    }

    @Before
    public void setup() {
        context = new AnnotationConfigApplicationContext();
        EnvironmentTestUtils.addEnvironment(context, "emily.retrofit.clients.test.base-url:http://localhost/");
        context.register(
                RetrofitAutoConfiguration.class,
                OkHttpAutoConfiguration.class,
                JacksonExtAutoConfiguration.class,
                ProviderAutoConfiguration.class
        );
        context.refresh();
    }

    @After
    public void teardown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    public void testRetrofitAutoConfigured() {
        RetrofitServiceFactory factory = context.getBean(RetrofitServiceFactory.class);
        Retrofit retrofit = factory.getRetrofit("test");
        assertThat(retrofit).isNotNull();
        assertThat(retrofit.baseUrl().toString()).isEqualTo("http://localhost/");

        // Assert that we have exactly the call adapter factories that are auto-configured
        List<CallAdapter.Factory> callAdapterFactories = retrofit.callAdapterFactories();

        // Retrofit internally adds its DefaultCallAdapterFactory
        assertThat(callAdapterFactories)
                .hasSize(4)
                .hasAtLeastOneElementOfType(Java8CallAdapterFactory.class)
                .hasAtLeastOneElementOfType(GuavaCallAdapterFactory.class)
                .hasAtLeastOneElementOfType(RxJava2CallAdapterFactory.class);

        // Assert that we have exactly the converter factories that are auto-configured
        List<Converter.Factory> converterFactories = retrofit.converterFactories();

        // Retrofit internally adds BuildInConverters
        assertThat(converterFactories)
                .hasSize(3)
                .hasAtLeastOneElementOfType(JacksonConverterFactory.class)
                .hasAtLeastOneElementOfType(ScalarsConverterFactory.class);

        MyService myService = factory.createServiceInstance(MyService.class, "test");
        assertThat(myService).isNotNull();
    }
}