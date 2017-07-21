package com.alice.emily.autoconfigure;

import com.alice.emily.module.resteasy.multipart.*;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 * Created by lianhao on 2017/3/29.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ StandardServletMultipartResolver.class,
        MessageBodyReader.class, MessageBodyWriter.class, MultipartConfigElement.class })
@ConditionalOnProperty(prefix = "spring.http.multipart", name = "enabled", matchIfMissing = true)
@AutoConfigureBefore(MultipartAutoConfiguration.class)
@EnableConfigurationProperties(MultipartProperties.class)
public class MultipartExtAutoConfiguration {

    private final MultipartProperties multipartProperties;

    public MultipartExtAutoConfiguration(MultipartProperties multipartProperties) {
        this.multipartProperties = multipartProperties;
    }

    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
    @ConditionalOnMissingBean(MultipartResolver.class)
    public MultipartServletResolver multipartResolver() {
        MultipartServletResolver multipartResolver = new MultipartServletResolver();
        multipartResolver.setResolveLazily(this.multipartProperties.isResolveLazily());
        return multipartResolver;
    }

    @Bean
    public MultipartFileReader multipartFileReader() {
        return new MultipartFileReader();
    }

    @Bean
    public MultipartFileArrayReader multipartFileArrayReader() {
        return new MultipartFileArrayReader();
    }

    @Bean
    public MultipartFileListReader multipartFileListReader() {
        return new MultipartFileListReader();
    }

    @Bean
    public MultipartFromBeanParamReader multipartFromBeanParamReader() {
        return new MultipartFromBeanParamReader();
    }
}
