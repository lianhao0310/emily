package com.alice.emily.swagger.plugin;

import com.alice.emily.validator.constraints.SRID;
import org.springframework.core.annotation.Order;
import springfox.bean.validators.plugins.Validators;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

/**
 * Created by lianhao on 2017/6/23.
 */
@Order(Validators.BEAN_VALIDATOR_PLUGIN_ORDER)
public class SRIDAnnotationPlugin implements AnnotationAwarePlugin {

    @Override
    public void apply(ParameterContext context) {
        getAnnotation(context, SRID.class)
                .ifPresent(srid -> {
                    boolean elementNullable = srid.elementNullable();
                    context.parameterBuilder()
                            .description("Must be valid EPSG " + srid.value()
                                    + " coordinate or geometry type (and it's array or collection)."
                                    + (elementNullable ? "" : "As for list or array, element cannot be null or empty!"));
                });
    }

    @Override
    public void apply(ModelPropertyContext context) {
        getAnnotation(context, SRID.class)
                .ifPresent(srid -> {
                    boolean elementNullable = srid.elementNullable();
                    context.getBuilder()
                            .description("Must be valid EPSG " + srid.value()
                                    + " coordinate or geometry type (and it's array or collection)."
                                    + (elementNullable ? "" : "As for list or array, element cannot be null or empty!"));
                });
    }

}
