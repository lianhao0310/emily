package com.alice.emily.data.mongodb;

import com.google.common.collect.Lists;
import com.alice.emily.data.mongodb.jts.JtsConverters;
import com.alice.emily.data.mongodb.support.MongoExtMappingContext;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by lianhao on 2017/6/7.
 */
@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class MongoMappingConfiguration {

    private final ApplicationContext applicationContext;
    private final MongoProperties properties;

    public MongoMappingConfiguration(ApplicationContext applicationContext,
                                     MongoProperties properties) {
        this.applicationContext = applicationContext;
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = Lists.newArrayList();
        converters.addAll(JtsConverters.geometryConverters());
        return new MongoCustomConversions(converters);
    }

    @Bean
    @ConditionalOnMissingBean
    public MongoExtMappingContext mongoMappingContext(MongoCustomConversions conversions) throws Exception {
        MongoExtMappingContext context = new MongoExtMappingContext();
        context.setInitialEntitySet(new EntityScanner(this.applicationContext)
                .scan(Document.class, Persistent.class));
        Class<?> strategyClass = this.properties.getFieldNamingStrategy();
        if (strategyClass != null) {
            FieldNamingStrategy strategy = (FieldNamingStrategy) BeanUtils.instantiateClass(strategyClass);
            context.setFieldNamingStrategy(strategy);
        }
        context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        return context;
    }

    @Bean
    @ConditionalOnMissingBean
    public MongoConverterInitializer mongoConverterInitializer() {
        return new MongoConverterInitializer();
    }

}
