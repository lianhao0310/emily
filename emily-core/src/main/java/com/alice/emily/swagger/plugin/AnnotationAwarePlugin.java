package com.alice.emily.swagger.plugin;

import springfox.bean.validators.plugins.Validators;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * Created by lianhao on 2017/6/23.
 */
public interface AnnotationAwarePlugin extends ParameterBuilderPlugin, ModelPropertyBuilderPlugin {

    @Override
    default boolean supports(DocumentationType delimiter) {
        // we simply support all documentationTypes!
        return true;
    }

    @Override
    void apply(ParameterContext context);

    default <T extends Annotation> Optional<T> getAnnotation(
            ParameterContext context,
            Class<T> annotationType) {
        return Validators.annotationFromParameter(context, annotationType).toJavaUtil();
    }

    default <T extends Annotation> Optional<T> getAnnotation(
            ModelPropertyContext context,
            Class<T> annotationType) {
        return Validators.extractAnnotation(context, annotationType).toJavaUtil();
    }
}
