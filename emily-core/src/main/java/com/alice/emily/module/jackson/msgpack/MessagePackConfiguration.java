package com.alice.emily.module.jackson.msgpack;

import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import lombok.extern.log4j.Log4j2;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.ws.rs.ext.MessageBodyReader;

/**
 * Created by lianhao on 2017/3/29.
 */
@Log4j2
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(MessagePackFactory.class)
public class MessagePackConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MessagePackMapper messagePackMapper() {
        MessagePackMapper objectMapper = new MessagePackMapper();
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        return objectMapper;
    }

    @Configuration
    static class SpringMVCSupport extends WebMvcConfigurerAdapter {
        @Override
        public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
            configurer.mediaType("msgpack", MediaType.valueOf("application/x-msgpack"));
        }
    }

    @Configuration
    @ConditionalOnClass(AbstractJackson2HttpMessageConverter.class)
    static class MessagePackConverterConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public MessagePackConverter messagePackConverter(MessagePackMapper objectMapper) {
            return new MessagePackConverter(objectMapper);
        }
    }

    @Configuration
    @ConditionalOnClass(MessageBodyReader.class)
    static class MessagePackProviderConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public MessagePackProvider messagePackProvider(MessagePackMapper objectMapper) {
            return new MessagePackProvider(objectMapper);
        }
    }
}