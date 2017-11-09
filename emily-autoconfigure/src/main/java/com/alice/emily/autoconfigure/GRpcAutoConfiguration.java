package com.alice.emily.autoconfigure;

import com.alice.emily.core.AbstractBeanPostProcessorsRegistrar;
import com.alice.emily.grpc.GRpcClientAnnotationBeanPostProcessor;
import com.alice.emily.grpc.GRpcClientFactory;
import com.alice.emily.grpc.GRpcProperties;
import com.alice.emily.grpc.GRpcServerBuilderConfigurer;
import com.alice.emily.grpc.GRpcServerRunner;
import com.alice.emily.grpc.GRpcService;
import io.grpc.BindableService;
import io.grpc.ServerBuilder;
import io.grpc.stub.AbstractStub;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ObjectUtils;

/**
 * Created by alexf on 25-Jan-16.
 */
@Configuration
@ConditionalOnClass({ AbstractStub.class })
@EnableConfigurationProperties(GRpcProperties.class)
@AutoConfigureOrder
@Import(GRpcAutoConfiguration.GRpcClientAnnotationBeanPostProcessorRegistrar.class)
public class GRpcAutoConfiguration {

    @Bean(name = "gRpcClientFactory")
    public GRpcClientFactory gRpcClientFactory(GRpcProperties configuration) {
        return new GRpcClientFactory(configuration);
    }

    @Configuration
    @ConditionalOnClass(ServerBuilder.class)
    static class GRpcServerAutoConfiguration {
        @Bean
        @ConditionalOnBean(value = BindableService.class, annotation = GRpcService.class)
        public GRpcServerRunner serverRunner(GRpcServerBuilderConfigurer configurer) {
            return new GRpcServerRunner(configurer);
        }

        @Bean
        @ConditionalOnMissingBean(GRpcServerBuilderConfigurer.class)
        public GRpcServerBuilderConfigurer serverBuilderConfigurer() {
            return new GRpcServerBuilderConfigurer();
        }
    }

    static class GRpcClientAnnotationBeanPostProcessorRegistrar
            extends AbstractBeanPostProcessorsRegistrar {

        @Override
        protected void doRegisterBeanDefinitions(
                AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
            Class<?> beanClass = GRpcClientAnnotationBeanPostProcessor.class;
            if (ObjectUtils.isEmpty(
                    this.beanFactory.getBeanNamesForType(beanClass, true, false))) {
                RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass);
                beanDefinition.setSynthetic(true);
                registry.registerBeanDefinition("gRpcClientAnnotationBeanPostProcessor", beanDefinition);
            }
        }

    }
}