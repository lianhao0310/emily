package com.alice.emily.autoconfigure;

import com.alice.emily.module.okhttp.OkHttpClientConfiguration;
import com.alice.emily.module.okhttp.OkHttpProperties;
import com.alice.emily.module.okhttp.OkHttpRestTemplateConfiguration;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for OkHttp.
 */
@Configuration
@ConditionalOnClass(OkHttpClient.class)
@Import({OkHttpClientConfiguration.class, OkHttpRestTemplateConfiguration.class})
@EnableConfigurationProperties(OkHttpProperties.class)
public class OkHttpAutoConfiguration {

}
