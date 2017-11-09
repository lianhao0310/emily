package com.alice.emily.resteasy.validation.plugins;

import com.alice.emily.core.SpringContext;
import org.jboss.resteasy.spi.validation.GeneralValidator;

import javax.validation.BootstrapConfiguration;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.executable.ExecutableType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.Set;

@Provider
public class ValidatorContextResolver implements ContextResolver<GeneralValidator> {

    @Override
    public GeneralValidator getContext(Class<?> type) {
        try {
            BootstrapConfiguration bootstrapConfiguration = Validation.byDefaultProvider()
                    .configure().getBootstrapConfiguration();

            boolean isExecutableValidationEnabled = bootstrapConfiguration.isExecutableValidationEnabled();

            Set<ExecutableType> defaultValidatedExecutableTypes =
                    bootstrapConfiguration.getDefaultValidatedExecutableTypes();

            return new GeneralValidatorImpl(SpringContext.validatorFactory(),
                    isExecutableValidationEnabled, defaultValidatedExecutableTypes);
        } catch (Exception e) {
            throw new ValidationException("Unable to load Validation support", e);
        }
    }
}