package com.alice.emily.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * Created by lianhao on 2017/3/17.
 */
@UtilityClass
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
     * @param type  目标类型
     * @param <T>    类型参数
     * @return 目标对象
     */
    @SneakyThrows
    public static <T> T parseObject(@NonNull String source, @NonNull Class<T> type) {
        return getMapper().readValue(source, type);
    }

    /**
     * 从源字符串中解析出Java对象列表
     *
     * @param source      源字符串
     * @param elementType 目标元素类型
     * @param <E>         类型参数
     * @return 目标对象列表
     */
    @SneakyThrows
    public static <E> List<E> parseArray(@NonNull String source, @NonNull Class<E> elementType) {
        CollectionType type = getMapper().getTypeFactory().constructCollectionType(List.class, elementType);
        return getMapper().readValue(source, type);
    }

    @SneakyThrows
    private static <T> T fromJson(@NonNull String source, @NonNull JavaType type) {
        return getMapper().readValue(source, type);
    }

    /**
     * 将Java对象转成Json字符串
     *
     * @param value Java对象
     * @return Json字符串
     */
    @SneakyThrows
    public static String toJson(@NonNull Object value) {
        return getMapper().writeValueAsString(value);
    }
}
