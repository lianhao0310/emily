package com.alice.emily.jackson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Maps;
import com.alice.emily.core.SpringBeans;
import com.alice.emily.jackson.msgpack.MessagePackMapper;
import com.alice.emily.test.TestPlainApplication;
import com.alice.emily.utils.JSON;
import com.vividsolutions.jts.geom.Coordinate;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by lianhao on 2017/4/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestPlainApplication.class)
public class JacksonWebEnvironmentTest {

    @Test
    public void testGetObjectMapper() {
        ObjectMapper mapper = SpringBeans.getBean(ObjectMapper.class);
        assertThat(mapper).isNotNull().isEqualTo(JSON.getObjectMapper());
        assertFeatureConfig(mapper);
        assertModules(mapper);
    }

    @Test
    public void testGetMessagePackMapper() {
        MessagePackMapper mapper = SpringBeans.getBean(MessagePackMapper.class);
        assertThat(mapper).isNotNull().isNotEqualTo(JSON.getObjectMapper());
        assertFeatureConfig(mapper);
        assertModules(mapper);
    }

    @Test
    public void testFeatures() {
        Employee employee = new Employee();
        employee.setName("Jack");
        employee.setAge(22);
        employee.setOther("address", "ShangHai");
        employee.setCoordinate(new Coordinate(30, 120));

        String jsonString = JSON.writeAsString(employee);
        assertThat(jsonString)
                .isNotBlank()
                .contains("\"address\" : \"ShangHai\"")
                .contains("\"x\" : 30.0")
                .doesNotContain("\"z\" :");
        assertThat(JSON.readObject(jsonString, Employee.class))
                .isNotNull()
                .isEqualToComparingFieldByFieldRecursively(employee);

    }

    private void assertFeatureConfig(ObjectMapper mapper) {
        assertThat(mapper.getSerializationConfig().getDefaultPropertyInclusion().getValueInclusion()).isEqualTo(JsonInclude.Include.NON_NULL);
        assertThat(mapper.getDeserializationConfig().isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)).isTrue();
        assertThat(mapper.getDeserializationConfig().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)).isFalse();
        assertThat(mapper.getSerializationConfig().isEnabled(SerializationFeature.INDENT_OUTPUT)).isTrue();
        assertThat(mapper.isEnabled(MapperFeature.USE_STATIC_TYPING)).isFalse();
        assertThat(mapper.isEnabled(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS)).isTrue();
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void assertModules(ObjectMapper mapper) {
        Object moduleTypes = FieldUtils.readField(mapper, "_registeredModuleTypes", true);
        assertThat(moduleTypes).isNotNull().isInstanceOf(Set.class);
        Set<Object> modules = (Set<Object>) moduleTypes;
        assertThat(modules)
                .contains("com.bedatadriven.jackson.datatype.jts.JtsModule")
                .contains("com.alice.emily.jackson.FuzzyEnumModule")
                .contains("com.alice.emily.jackson.GuavaExtrasModule")
                .contains("com.fasterxml.jackson.datatype.guava.GuavaModule")
                .contains("com.fasterxml.jackson.datatype.jdk8.Jdk8Module")
                .contains("com.fasterxml.jackson.datatype.jsr310.JSR310Module")
                .contains("com.fasterxml.jackson.module.paramnames.ParameterNamesModule");
    }

    @Data
    public static class Employee {
        private String name;
        private int age;
        private Coordinate coordinate;

        @JsonIgnore
        private Map<String, Object> other = Maps.newHashMap();

        @JsonAnyGetter
        public Map<String, Object> getOther() {
            return other;
        }

        @JsonAnySetter
        public void setOther(String name, Object value) {
            other.put(name, value);
        }
    }
}
