package com.alice.emily.autoconfigure;

import com.alice.emily.module.grpc.*;
import io.grpc.ServerBuilder;
import io.grpc.stub.AbstractStub;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by alexf on 25-Jan-16.
 */
@Configuration
@ConditionalOnClass({ AbstractStub.class })
@EnableConfigurationProperties(GRpcProperties.class)
@AutoConfigureOrder
public class GRpcAutoConfiguration {

    @Bean(name = "gRpcClientFactory")
    public GRpcClientFactory gRpcClientFactory(GRpcProperties configuration) {
        return new GRpcClientFactory(configuration);
    }

    @Bean
    public GRpcClientBeanPostProcessor gRpcClientBeanPostProcessor(GRpcClientFactory clientFactory) {
        return new GRpcClientBeanPostProcessor(clientFactory);
    }

    @Configuration
    @ConditionalOnClass(ServerBuilder.class)
    class GRpcServerAutoConfiguration {
        @Bean
        @ConditionalOnBean(annotation = GRpcService.class)
        public GRpcServerRunner serverRunner(GRpcServerBuilderConfigurer configurer) {
            return new GRpcServerRunner(configurer);
        }

        @Bean
        @ConditionalOnMissingBean(GRpcServerBuilderConfigurer.class)
        public GRpcServerBuilderConfigurer serverBuilderConfigurer() {
            return new GRpcServerBuilderConfigurer();
        }
    }
}