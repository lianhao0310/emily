package com.alice.emily.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;

import java.util.List;

/**
 * Created by liupin on 2017/3/17.
 */
public class JSONUtils {

    private static volatile ObjectMapper objectMapper;

    public static synchronized void setMapper(ObjectMapper objectMapper) {
        JSONUtils.objectMapper = objectMapper;
    }

    public static ObjectMapper getMapper() {
        if (objectMapper == null) {
            synchronized (JSONUtils.class) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                    objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
                }
            }
        }
        return objectMapper;
    }

    /**
     * 从源字符串中解析出Java对象
     *
     * @param source 源字符串
     * @param clazz  目标类型
     * @param <T>    类型参数
     * @return 目标对象
     */
    public static <T> T fromJsonObject(String source, Class<T> clazz) {
        Preconditions.checkNotNull(clazz, "class type must be provided");
        JavaType javaType = getMapper().getTypeFactory().constructType(clazz);
        return fromJson(source, javaType);
    }

    /**
     * 从源字符串中解析出Java对象列表
     *
     * @param source      源字符串
     * @param elementType 目标元素类型
     * @param <E>         类型参数
     * @return 目标对象列表
     */
    public static <E> List<E> fromJsonArray(String source, Class<E> elementType) {
        Preconditions.checkNotNull(elementType, "element type must be provided");
        CollectionType type = getMapper().getTypeFactory().constructCollectionType(List.class, elementType);
        return fromJson(source, type);
    }

    @SneakyThrows
    private static <T> T fromJson(String source, JavaType type) {
        Preconditions.checkNotNull(source, "source must be provided");
        Preconditions.checkNotNull(type, "java type must be provided");
        return getMapper().readValue(source, type);
    }

    /**
     * 将Java对象转成Json字符串
     *
     * @param value Java对象
     * @return Json字符串
     */
    @SneakyThrows
    public static String toJson(Object value) {
        Preconditions.checkNotNull(value, "object value must be provided");
        return getMapper().writeValueAsString(value);
    }
}
