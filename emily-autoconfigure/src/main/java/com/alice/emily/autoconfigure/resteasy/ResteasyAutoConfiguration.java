package com.alice.emily.autoconfigure.resteasy;

import com.alice.emily.autoconfigure.core.JacksonExtAutoConfiguration;
import com.alice.emily.resteasy.ResteasyClientConfiguration;
import com.alice.emily.resteasy.ResteasyConfiguration;
import com.alice.emily.resteasy.ResteasyProperties;
import com.alice.emily.web.HttpCorsConfiguration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.springmvc.ResteasyHandlerMapping;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by lianhao on 2017/2/9.
 */
@Configuration
@AutoConfigureAfter(JacksonExtAutoConfiguration.class)
@EnableConfigurationProperties(ResteasyProperties.class)
public class ResteasyAutoConfiguration {

    @Configuration
    @ConditionalOnClass({ ResteasyDeployment.class, ResteasyHandlerMapping.class })
    @Import({ ResteasyConfiguration.class, HttpCorsConfiguration.class })
    static class ContainerConfiguration {
    }

    @Configuration
    @ConditionalOnClass({ ResteasyClient.class })
    @Import({ ResteasyClientConfiguration.class })
    static class ClientConfiguration {
    }

}
