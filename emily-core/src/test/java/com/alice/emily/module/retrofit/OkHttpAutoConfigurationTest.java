package com.palmaplus.euphoria.module.retrofit;

import com.palmaplus.euphoria.autoconfigure.OkHttpAutoConfiguration;
import okhttp3.OkHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link OkHttpAutoConfiguration}
 */
public class OkHttpAutoConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setup() {
        loadContext();
    }

    @After
    public void teardown() {
        if (context != null) {
            context.close();
        }
    }

    public static class MyOkHttpConfiguration {
        private OkHttpClient mockClient = mock(OkHttpClient.class);

        @Bean
        public OkHttpClient okHttpClient() {
            return mockClient;
        }
    }

    @Test
    public void testOkHttpClientAutoConfigured() {
        OkHttpClient okHttpClient = context.getBean(OkHttpClient.class);

        assertThat(okHttpClient).isNotNull();
        assertThat(okHttpClient.connectTimeoutMillis()).isEqualTo(10000);
        assertThat(okHttpClient.readTimeoutMillis()).isEqualTo(10000);
        assertThat(okHttpClient.writeTimeoutMillis()).isEqualTo(10000);
    }

    @Test
    public void testOkHttpClientAutoConfiguredWithCustomProperties() {
        context = new AnnotationConfigApplicationContext();
        context.register(OkHttpAutoConfiguration.class);
        EnvironmentTestUtils.addEnvironment(context, "euphoria.okhttp.connection-timeout:500");
        EnvironmentTestUtils.addEnvironment(context, "euphoria.okhttp.read-timeout:600");
        EnvironmentTestUtils.addEnvironment(context, "euphoria.okhttp.write-timeout:700");
        context.refresh();

        OkHttpClient okHttpClient = context.getBean(OkHttpClient.class);

        assertThat(okHttpClient).isNotNull();
        assertThat(okHttpClient.connectTimeoutMillis()).isEqualTo(500);
        assertThat(okHttpClient.readTimeoutMillis()).isEqualTo(600);
        assertThat(okHttpClient.writeTimeoutMillis()).isEqualTo(700);
    }

    @Test
    public void testOkHttpClientOverridingBean() {
        context = new AnnotationConfigApplicationContext();
        context.register(OkHttpAutoConfiguration.class, MyOkHttpConfiguration.class);
        context.refresh();

        OkHttpClient okHttpClient = context.getBean(OkHttpClient.class);
        MyOkHttpConfiguration myOkHttpConfiguration = context.getBean(MyOkHttpConfiguration.class);

        assertThat(okHttpClient).isEqualTo(myOkHttpConfiguration.mockClient);
    }

    private void loadContext() {
        context = new AnnotationConfigApplicationContext();
        context.register(OkHttpAutoConfiguration.class);
        context.refresh();
    }
}