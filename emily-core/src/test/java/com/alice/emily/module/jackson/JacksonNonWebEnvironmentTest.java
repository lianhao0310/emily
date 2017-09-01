package com.palmaplus.euphoria.module.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.palmaplus.euphoria.core.BeanProvider;
import com.palmaplus.euphoria.module.jackson.msgpack.MessagePackMapper;
import com.palmaplus.euphoria.utils.JSONUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by liupin on 2017/4/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class JacksonNonWebEnvironmentTest {

    @Test
    public void testGetObjectMapper() {
        ObjectMapper mapper = BeanProvider.getBean(ObjectMapper.class);
        assertThat(mapper).isNotNull().isEqualTo(JSONUtils.getMapper());
        assertThat(mapper.getSerializationConfig().getDefaultPropertyInclusion().getValueInclusion()).isEqualTo(JsonInclude.Include.NON_NULL);
        assertThat(mapper.getDeserializationConfig().isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)).isFalse();
        assertThat(mapper.getDeserializationConfig().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)).isFalse();
        assertThat(mapper.getSerializationConfig().isEnabled(SerializationFeature.INDENT_OUTPUT)).isTrue();
        assertThat(mapper.isEnabled(MapperFeature.USE_STATIC_TYPING)).isFalse();
    }

    @Test
    public void testGetMessagePackMapper() {
        MessagePackMapper mapper = BeanProvider.getBean(MessagePackMapper.class);
        assertThat(mapper).isNull();
    }
}
