package com.alice.emily.convert;

import org.jooq.lambda.Unchecked;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ConversionServiceConfigurerInitializer implements InitializingBean {

    @Autowired(required = false)
    private List<ConversionService> conversionServices;

    @Autowired(required = false)
    private List<ConversionServiceConfigurer> configurers;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isEmpty(conversionServices)
                || CollectionUtils.isEmpty(configurers)) {
            return;
        }
        for (ConversionServiceConfigurer configurer : configurers) {
            conversionServices.forEach(Unchecked.consumer(configurer::configure));
        }
    }
}