package com.alice.emily.jackson.msgpack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import lombok.extern.log4j.Log4j2;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import javax.ws.rs.ext.MessageBodyReader;

/**
 * Created by lianhao on 2017/3/29.
 */
@Log4j2
@Configuration
@ConditionalOnClass({ MessagePackFactory.class, ObjectMapper.class })
public class MessagePackConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MessagePackMapper messagePackMapper() {
        MessagePackMapper messagePackMapper = new MessagePackMapper();
        messagePackMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        return messagePackMapper;
    }

    @Configuration
    @ConditionalOnClass(CodecConfigurer.class)
    static class MessagePackWebFluxConfiguration {
        @Bean
        public CodecCustomizer messagePackCodecCustomizer(MessagePackMapper messagePackMapper) {
            return configurer -> MessagePackCodecSupport.register(configurer, messagePackMapper);
        }
    }

    @Configuration
    @ConditionalOnWebApplication(type = Type.SERVLET)
    static class MessagePackWebServletConfiguration {

        @Configuration
        @ConditionalOnClass(AbstractJackson2HttpMessageConverter.class)
        static class SpringMVCConfiguration {
            @Bean
            @ConditionalOnMissingBean
            public MappingJackson2MsgpackHttpMessageConverter messagePackConverter(MessagePackMapper messagePackMapper) {
                return new MappingJackson2MsgpackHttpMessageConverter(messagePackMapper);
            }
        }

        @Configuration
        @ConditionalOnClass({ MessageBodyReader.class, JacksonJsonProvider.class })
        static class JAXRSConfiguration {
            @Bean
            @ConditionalOnMissingBean
            public MessagePackProvider messagePackProvider(MessagePackMapper objectMapper) {
                return new MessagePackProvider(objectMapper);
            }
        }

    }

}