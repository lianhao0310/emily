package com.alice.emily.module.resteasy.validation.plugins;

import com.alice.emily.core.BeanProvider;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.jboss.resteasy.spi.validation.GeneralValidator;

import javax.validation.*;
import javax.validation.executable.ExecutableType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.Set;

@Log4j2
@Provider
public class ValidatorContextResolver implements ContextResolver<GeneralValidator> {

    private LazyInitializer<ValidatorFactory> holder = new LazyInitializer<ValidatorFactory>() {
        @Override
        protected ValidatorFactory initialize() throws ConcurrentException {
            ValidatorFactory validatorFactory = BeanProvider.getBean(ValidatorFactory.class);
            log.info("Obtaining Spring-bean enabled validator factory: {}", validatorFactory);
            return validatorFactory;
        }
    };

    @Override
    public GeneralValidator getContext(Class<?> type) {
        try {
            Configuration<?> config = Validation.byDefaultProvider().configure();
            BootstrapConfiguration bootstrapConfiguration = config.getBootstrapConfiguration();
            boolean isExecutableValidationEnabled = bootstrapConfiguration.isExecutableValidationEnabled();
            Set<ExecutableType> defaultValidatedExecutableTypes = bootstrapConfiguration.getDefaultValidatedExecutableTypes();
            return new GeneralValidatorImpl(holder.get(), isExecutableValidationEnabled, defaultValidatedExecutableTypes);
        } catch (Exception e) {
            throw new ValidationException("Unable to load Validation support", e);
        }
    }
}