package com.alice.emily.web.param;

import com.alice.emily.core.SpringBeans;
import com.alice.emily.utils.Lazy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Method processor supports {@link ParamName} parameters renaming
 */
public class RenamingModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor {

    private static final Field BINDING_RESULT_FIELD = FieldUtils.getField(DataBinder.class, "bindingResult", true);

    private final Lazy<RequestMappingHandlerAdapter> handlerAdapter =
            Lazy.of(() -> SpringBeans.getBean(RequestMappingHandlerAdapter.class));

    private final Map<Class<?>, Map<String, String>> renameCache = new ConcurrentHashMap<>();

    public RenamingModelAttributeMethodProcessor(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (!super.supportsParameter(parameter)) {
            return false;
        }
        if (ClassUtils.getPackageName(parameter.getParameterType()).startsWith("org.springframework")) {
            return false;
        }
        Map<String, String> mapping = getMapping(parameter.getParameterType());
        return !CollectionUtils.isEmpty(mapping);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest nativeWebRequest) {
        Object target = binder.getTarget();
        if (target == null) {
            return;
        }
        Map<String, String> mapping = getMapping(target.getClass());
        if (!CollectionUtils.isEmpty(mapping)) {
            bindWithMapping(binder, nativeWebRequest, mapping);
        } else {
            super.bindRequestParameters(binder, nativeWebRequest);
        }
    }

    @SneakyThrows
    private void bindWithMapping(WebDataBinder binder,
                                 NativeWebRequest nativeWebRequest, Map<String, String> mapping) {
        WebBindingInitializer initializer = handlerAdapter.get().getWebBindingInitializer();
        Assert.notNull(initializer, "WebBindingInitializer must be provided!");
        ParamNameDataBinder paramNameDataBinder =
                new ParamNameDataBinder(binder.getTarget(), binder.getObjectName(), mapping);
        initializer.initBinder(paramNameDataBinder);
        super.bindRequestParameters(paramNameDataBinder, nativeWebRequest);
        BindingResult bindingResult = paramNameDataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            FieldUtils.writeField(BINDING_RESULT_FIELD, binder, bindingResult, true);
        }
    }

    private Map<String, String> getMapping(Class<?> targetClass) {
        return renameCache.computeIfAbsent(targetClass, tc -> {
            Map<String, String> mapping = analyzeClass(tc);
            if (CollectionUtils.isEmpty(mapping)) {
                mapping = Collections.emptyMap();
            }
            return mapping;
        });
    }

    private Map<String, String> analyzeClass(Class<?> targetClass) {
        Map<String, String> renameMap = new HashMap<>();
        ReflectionUtils.doWithFields(targetClass, field -> {
            ParamName paramNameAnnotation = field.getAnnotation(ParamName.class);
            if (paramNameAnnotation != null && !paramNameAnnotation.value().isEmpty()) {
                renameMap.put(paramNameAnnotation.value(), field.getName());
            }
        }, field -> field.isAnnotationPresent(ParamName.class));
        return renameMap.isEmpty() ? null : Collections.unmodifiableMap(renameMap);
    }
}