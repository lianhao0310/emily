package com.alice.emily.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.convert.JodaTimeConverters;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.convert.ThreeTenBackPortConverters;
import org.springframework.data.geo.format.DistanceFormatter;
import org.springframework.data.geo.format.PointFormatter;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by lianhao on 2017/7/4.
 */
public class DefaultConversionServiceConfigurer implements ConversionServiceConfigurer {

    private static final boolean IS_SPRING_DATA_PRESENT =
            ClassUtils.isPresent("org.springframework.data.repository.CrudRepository",
                    ClassUtils.getDefaultClassLoader());

    @Autowired(required = false)
    private List<FormatterRegistrar> formatterRegistrars;

    @Autowired(required = false)
    private List<Converter<?, ?>> converters;

    @Autowired(required = false)
    private List<GenericConverter> genericConverters;

    @Override
    public void configure(ConversionService conversionService) throws Exception {
        if (conversionService instanceof ConfigurableConversionService) {
            configureConverter((ConfigurableConversionService) conversionService);
        }
        if (conversionService instanceof FormattingConversionService) {
            configureFormatter((FormattingConversionService) conversionService);
        }
    }

    protected void configureConverter(ConfigurableConversionService ccs) {
        CommonConverters.getConvertersToRegister().forEach(ccs::addConverter);

        if (IS_SPRING_DATA_PRESENT) {
            Jsr310Converters.getConvertersToRegister().forEach(ccs::addConverter);
            JodaTimeConverters.getConvertersToRegister().forEach(ccs::addConverter);
            ThreeTenBackPortConverters.getConvertersToRegister().forEach(ccs::addConverter);
        }

        if (!CollectionUtils.isEmpty(converters)) {
            converters.forEach(ccs::addConverter);
        }

        if (!CollectionUtils.isEmpty(genericConverters)) {
            genericConverters.forEach(ccs::addConverter);
        }
    }

    protected void configureFormatter(FormattingConversionService fcs) {
        if (IS_SPRING_DATA_PRESENT) {
            fcs.addFormatter(DistanceFormatter.INSTANCE);
            fcs.addFormatter(PointFormatter.INSTANCE);
        }

        if (!CollectionUtils.isEmpty(formatterRegistrars)) {
            for (FormatterRegistrar formatterRegistrar : formatterRegistrars) {
                formatterRegistrar.registerFormatters(fcs);
            }
        }
    }
}
