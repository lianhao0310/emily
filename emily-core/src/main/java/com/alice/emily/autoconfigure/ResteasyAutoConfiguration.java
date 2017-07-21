package com.alice.emily.autoconfigure;

import com.alice.emily.module.resteasy.ResteasyClientConfiguration;
import com.alice.emily.module.resteasy.ResteasyConfiguration;
import com.alice.emily.module.resteasy.ResteasyProperties;
import com.alice.emily.module.resteasy.cors.HttpCorsConfiguration;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.springmvc.ResteasyHandlerMapping;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by liupin on 2017/2/9.
 */
@Configuration
@ConditionalOnClass({ ResteasyDeployment.class, ResteasyHandlerMapping.class })
@Import({ ResteasyConfiguration.class, HttpCorsConfiguration.class, ResteasyClientConfiguration.class })
@AutoConfigureAfter(JacksonExtAutoConfiguration.class)
@EnableConfigurationProperties(ResteasyProperties.class)
public class ResteasyAutoConfiguration {

}
