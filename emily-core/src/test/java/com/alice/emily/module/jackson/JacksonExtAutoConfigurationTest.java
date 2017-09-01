package com.palmaplus.euphoria.module.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.palmaplus.euphoria.autoconfigure.JacksonExtAutoConfiguration;
import com.palmaplus.euphoria.utils.JSONUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by liupin on 2017/3/31.
 */
public class JacksonExtAutoConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setup() {
        context = new AnnotationConfigApplicationContext();
        context.register(JacksonExtAutoConfiguration.class);
    }

    @After
    public void teardown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    public void testJacksonAutoConfigured() {
        context.refresh();
        ObjectMapper mapper = context.getBean(ObjectMapper.class);
        assertThat(mapper).isNotNull();
        assertThat(mapper).isEqualTo(JSONUtils.getMapper());
        assertThat(mapper.getPropertyNamingStrategy()).isInstanceOf(AnnotationSensitivePropertyNamingStrategy.class);
    }

    @Test
    public void testsJacksonAutoConfiguredWithCustomProperties() {
        EnvironmentTestUtils.addEnvironment(context, "spring.jackson.default-property-inclusion=non_null");
        EnvironmentTestUtils.addEnvironment(context, "spring.jackson.deserialization.accept-float-as-int=false");
        EnvironmentTestUtils.addEnvironment(context, "spring.jackson.deserialization.fail-on-unknown-properties=false");
        EnvironmentTestUtils.addEnvironment(context, "spring.jackson.serialization.indent_output=true");
        EnvironmentTestUtils.addEnvironment(context, "spring.jackson.mapper.use-static-typing=false");
        context.refresh();

        ObjectMapper mapper = context.getBean(ObjectMapper.class);
        assertThat(mapper).isNotNull();
        assertThat(mapper).isEqualTo(JSONUtils.getMapper());
        assertThat(mapper.getSerializationConfig().getDefaultPropertyInclusion().getValueInclusion()).isEqualTo(JsonInclude.Include.NON_NULL);
        assertThat(mapper.getDeserializationConfig().isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)).isFalse();
        assertThat(mapper.getDeserializationConfig().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)).isFalse();
        assertThat(mapper.getSerializationConfig().isEnabled(SerializationFeature.INDENT_OUTPUT)).isTrue();
        assertThat(mapper.isEnabled(MapperFeature.USE_STATIC_TYPING)).isFalse();
    }
}
