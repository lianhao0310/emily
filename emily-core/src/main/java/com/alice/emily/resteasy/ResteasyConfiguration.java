package com.alice.emily.resteasy;

import com.alice.emily.core.SpringBeans;
import com.alice.emily.resteasy.provider.ProviderConfiguration;
import org.jboss.resteasy.plugins.spring.SpringBeanProcessor;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.springmvc.ResteasyHandlerAdapter;
import org.jboss.resteasy.springmvc.ResteasyHandlerMapping;
import org.jboss.resteasy.springmvc.ResteasyNoResourceFoundView;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Collection;
import java.util.Properties;

/**
 * Created by lianhao on 2017/4/9.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ ResteasyDeployment.class, ResteasyHandlerMapping.class })
@Import({ ProviderConfiguration.class })
public class ResteasyConfiguration {

    @Bean(name = "resteasy.deployment", initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean(ResteasyDeployment.class)
    @ConfigurationProperties(prefix = "emily.resteasy")
    public ResteasyDeployment resteasyDeployment() {
        return new ResteasyDeployment();
    }

    @Bean(name = "resteasy.spring.bean.processor")
    @ConditionalOnMissingBean(SpringBeanProcessor.class)
    public static SpringBeanProcessor springBeanProcessor(ResteasyDeployment deployment) {
        return new SpringBeanProcessor(deployment);
    }

    @Bean(name = "resteasy.handlerMapping")
    @ConditionalOnMissingBean(ResteasyHandlerMapping.class)
    public ResteasyHandlerMapping resteasyHandlerMapping(ResteasyDeployment deployment) {
        ResteasyHandlerMapping handlerMapping = new ResteasyHandlerMapping(deployment);
        handlerMapping.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
        Collection<HandlerInterceptor> interceptors = SpringBeans.getBeans(HandlerInterceptor.class);
        if (!CollectionUtils.isEmpty(interceptors)) {
            handlerMapping.setInterceptors(interceptors.toArray(new HandlerInterceptor[interceptors.size()]));
        }
        return handlerMapping;
    }

    @Bean(name = "resteasy.handlerAdapter")
    @ConditionalOnMissingBean(ResteasyHandlerAdapter.class)
    public ResteasyHandlerAdapter resteasyHandlerAdapter(ResteasyDeployment deployment) {
        return new ResteasyHandlerAdapter(deployment);
    }

    @Bean(name = "resteasy.no.resource.found.view")
    @ConditionalOnMissingBean(ResteasyNoResourceFoundView.class)
    public ResteasyNoResourceFoundView resteasyNoResourceFoundView(ResteasyDeployment deployment) {
        ResteasyNoResourceFoundView resteasyNoResourceFoundView = new ResteasyNoResourceFoundView();
        resteasyNoResourceFoundView.setDeployment(deployment);
        return resteasyNoResourceFoundView;
    }

    @Bean(name = "resteasy.exception.handler")
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        Properties properties = new Properties();
        properties.put("org.jboss.resteasy.spi.NoResourceFoundFailure", "resteasy.no.resource.found.view");
        SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
        simpleMappingExceptionResolver.setExceptionMappings(properties);
        simpleMappingExceptionResolver.setExceptionAttribute("exception");
        return simpleMappingExceptionResolver;
    }
}
