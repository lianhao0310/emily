package com.alice.emily.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.alice.emily.swagger.plugin.OperationPageableParameterReader;
import com.alice.emily.swagger.plugin.ParamNameAnnotationPlugin;
import com.alice.emily.swagger.plugin.PositiveNegativeAnnotationPlugin;
import com.alice.emily.swagger.plugin.PseudoMercatorAnnotationPlugin;
import com.alice.emily.swagger.plugin.SRIDAnnotationPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.schema.TypeNameExtractor;

/**
 * Created by lianhao on 2017/6/23.
 */
@Configuration
public class SpringFoxExtPluginConfiguration {

    @Bean
    public ParamNameAnnotationPlugin parameterNamePlugin() {
        return new ParamNameAnnotationPlugin();
    }

    @Bean
    public PositiveNegativeAnnotationPlugin positiveNegativeAnnotationPlugin() {
        return new PositiveNegativeAnnotationPlugin();
    }

    @Bean
    public PseudoMercatorAnnotationPlugin pseudoMercatorAnnotationPlugin() {
        return new PseudoMercatorAnnotationPlugin();
    }

    @Bean
    public SRIDAnnotationPlugin sridAnnotationPlugin() {
        return new SRIDAnnotationPlugin();
    }

    @Configuration
    @ConditionalOnClass(Pageable.class)
    static class SpringDataSupportPluginConfiguration {

        @Bean
        public OperationPageableParameterReader operationPageableParameterReader(
                TypeNameExtractor nameExtractor, TypeResolver resolver) {
            return new OperationPageableParameterReader(nameExtractor, resolver);
        }

    }

}
