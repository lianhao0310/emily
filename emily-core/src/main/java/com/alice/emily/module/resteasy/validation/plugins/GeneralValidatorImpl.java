package com.alice.emily.module.resteasy.validation.plugins;

import com.fasterxml.classmate.*;
import com.fasterxml.classmate.members.RawMethod;
import com.fasterxml.classmate.members.ResolvedMethod;
import com.alice.emily.module.resteasy.validation.api.ResteasyViolationException;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyConfiguration;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.validation.GeneralValidator;
import org.jboss.resteasy.util.GetRestful;

import javax.validation.*;
import javax.validation.executable.ExecutableType;
import javax.validation.executable.ValidateOnExecution;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author <a href="ron.sigal@jboss.com">Ron Sigal</a>
 * @version $Revision: 1.1 $
 *          <p>
 *          Copyright May 23, 2013
 */
public class GeneralValidatorImpl implements GeneralValidator {
    public static final String SUPPRESS_VIOLATION_PATH = "resteasy.validation.suppress.path";

    /**
     * Used for resolving type parameters. Thread-safe.
     */
    private TypeResolver typeResolver = new TypeResolver();
    private ValidatorFactory validatorFactory;
    private boolean isExecutableValidationEnabled;
    private ExecutableType[] defaultValidatedExecutableTypes;
    private boolean suppressPath;

    public GeneralValidatorImpl(ValidatorFactory validatorFactory, boolean isExecutableValidationEnabled, Set<ExecutableType> defaultValidatedExecutableTypes) {
        this.validatorFactory = validatorFactory;
        this.isExecutableValidationEnabled = isExecutableValidationEnabled;
        this.defaultValidatedExecutableTypes = defaultValidatedExecutableTypes.toArray(new ExecutableType[]{ });

        ResteasyConfiguration context = ResteasyProviderFactory.getContextData(ResteasyConfiguration.class);
        if (context != null) {
            String s = context.getParameter(SUPPRESS_VIOLATION_PATH);
            if (s != null) {
                suppressPath = Boolean.parseBoolean(s);
            }
        }
    }

    @Override
    public void validate(HttpRequest request, Object object, Class<?>... groups) {
        Validator validator = getValidator(request);
        Set<ConstraintViolation<Object>> cvs;

        try {
            cvs = validator.validate(object, groups);
        } catch (Exception e) {
            SimpleViolationsContainer violationsContainer = getViolationsContainer(request, object);
            violationsContainer.setException(e);
            violationsContainer.setFieldsValidated(true);
            throw new ResteasyViolationException(violationsContainer);
        }

        SimpleViolationsContainer violationsContainer = getViolationsContainer(request, object);
        violationsContainer.addViolations(cvs);
        violationsContainer.setFieldsValidated(true);
    }

    @Override
    public void checkViolations(HttpRequest request) {
        // Called from resteasy-jaxrs only if two argument version of isValidatable() returns true.
        SimpleViolationsContainer violationsContainer = getViolationsContainer(request, null);
        Object target = violationsContainer.getTarget();
        if (target != null && violationsContainer.isFieldsValidated()) {
            if (violationsContainer.size() > 0) {
                throw new ResteasyViolationException(violationsContainer, request.getHttpHeaders().getAcceptableMediaTypes());
            }
        }
    }

    @Override
    public void validateAllParameters(HttpRequest request, Object object, Method method, Object[] parameterValues, Class<?>... groups) {
        Validator validator = getValidator(request);
        SimpleViolationsContainer violationsContainer = getViolationsContainer(request, object);

        if (method.getParameterTypes().length == 0) {
            checkViolations(request);
            return;
        }

        Set<ConstraintViolation<Object>> cvs;

        try {
            cvs = validator.forExecutables().validateParameters(object, method, parameterValues, groups);
        } catch (Exception e) {
            violationsContainer.setException(e);
            throw new ResteasyViolationException(violationsContainer);
        }
        violationsContainer.addViolations(cvs);
        if ((violationsContainer.isFieldsValidated()
                || !GetRestful.isRootResource(object.getClass()))
                && violationsContainer.size() > 0) {
            throw new ResteasyViolationException(violationsContainer, request.getHttpHeaders().getAcceptableMediaTypes());
        }
    }

    @Override
    public void validateReturnValue(HttpRequest request, Object object, Method method, Object returnValue, Class<?>... groups) {
        Validator validator = getValidator(request);
        SimpleViolationsContainer violationsContainer = getViolationsContainer(request, object);
        Set<ConstraintViolation<Object>> cvs;

        try {
            cvs = validator.forExecutables().validateReturnValue(object, method, returnValue, groups);
        } catch (Exception e) {
            violationsContainer.setException(e);
            throw new ResteasyViolationException(violationsContainer);
        }
        violationsContainer.addViolations(cvs);
        if (violationsContainer.size() > 0) {
            throw new ResteasyViolationException(violationsContainer, request.getHttpHeaders().getAcceptableMediaTypes());
        }
    }

    @Override
    public boolean isValidatable(Class<?> clazz) {
        return true;
    }

    @Override
    public boolean isMethodValidatable(Method m) {
        if (!isExecutableValidationEnabled) {
            return false;
        }

        ExecutableType[] types;
        List<ExecutableType[]> typesList = getExecutableTypesOnMethodInHierarchy(m);
        if (typesList.size() > 1) {
            throw new ValidationException("@ValidateOnExecution found on multiple overridden methods");
        }
        if (typesList.size() == 1) {
            types = typesList.get(0);
        } else {
            ValidateOnExecution voe = m.getDeclaringClass().getAnnotation(ValidateOnExecution.class);
            if (voe == null) {
                types = defaultValidatedExecutableTypes;
            } else {
                if (voe.type().length > 0) {
                    types = voe.type();
                } else {
                    types = defaultValidatedExecutableTypes;
                }
            }
        }

        boolean isGetterMethod = isGetter(m);
        for (ExecutableType type : types) {
            switch (type) {
                case IMPLICIT:
                case ALL:
                    return true;

                case NONE:
                    continue;

                case NON_GETTER_METHODS:
                    if (!isGetterMethod) {
                        return true;
                    }
                    continue;

                case GETTER_METHODS:
                    if (isGetterMethod) {
                        return true;
                    }
                    continue;

                default:
            }
        }
        return false;
    }

    protected List<ExecutableType[]> getExecutableTypesOnMethodInHierarchy(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        List<ExecutableType[]> typesList = new ArrayList<ExecutableType[]>();

        while (clazz != null) {
            // We start by examining the method itself.
            Method superMethod = getSuperMethod(method, clazz);
            if (superMethod != null) {
                ExecutableType[] types = getExecutableTypesOnMethod(superMethod);
                if (types != null) {
                    typesList.add(types);
                }
            }

            typesList.addAll(getExecutableTypesOnMethodInInterfaces(clazz, method));
            clazz = clazz.getSuperclass();
        }
        return typesList;
    }

    protected List<ExecutableType[]> getExecutableTypesOnMethodInInterfaces(Class<?> clazz, Method method) {
        List<ExecutableType[]> typesList = new ArrayList<ExecutableType[]>();
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            Method interfaceMethod = getSuperMethod(method, anInterface);
            if (interfaceMethod != null) {
                ExecutableType[] types = getExecutableTypesOnMethod(interfaceMethod);
                if (types != null) {
                    typesList.add(types);
                }
            }
            List<ExecutableType[]> superList = getExecutableTypesOnMethodInInterfaces(anInterface, method);
            if (superList.size() > 0) {
                typesList.addAll(superList);
            }
        }
        return typesList;
    }

    protected static ExecutableType[] getExecutableTypesOnMethod(Method method) {
        ValidateOnExecution voe = method.getAnnotation(ValidateOnExecution.class);
        if (voe == null || voe.type().length == 0) {
            return null;
        }
        ExecutableType[] types = voe.type();
        if (types.length == 0) {
            return null;
        }
        return types;
    }

    protected static boolean isGetter(Method m) {
        String name = m.getName();
        Class<?> returnType = m.getReturnType();
        if (returnType.equals(Void.class)) {
            return false;
        }
        if (m.getParameterTypes().length > 0) {
            return false;
        }
        if (name.startsWith("get")) {
            return true;
        }
        if (name.startsWith("is") && returnType.equals(boolean.class)) {
            return true;
        }
        return false;
    }

    /**
     * Returns a super method, if any, of a method in a class.
     * Here, the "super" relationship is reflexive.  That is, a method
     * is a super method of itself.
     */
    protected Method getSuperMethod(Method method, Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if (overrides(method, methods[i])) {
                return methods[i];
            }
        }
        return null;
    }

    /**
     * Checks, whether {@code subTypeMethod} overrides {@code superTypeMethod}.
     * <p>
     * N.B. "Override" here is reflexive. I.e., a method overrides itself.
     *
     * @param subTypeMethod   The sub type method (cannot be {@code null}).
     * @param superTypeMethod The super type method (cannot be {@code null}).
     * @return Returns {@code true} if {@code subTypeMethod} overrides {@code superTypeMethod}, {@code false} otherwise.
     * <p>
     * Taken from Hibernate Validator
     */
    protected boolean overrides(Method subTypeMethod, Method superTypeMethod) {
        if (subTypeMethod == null || superTypeMethod == null) {
            throw new RuntimeException("Expect two non-null methods");
        }

        if (!subTypeMethod.getName().equals(superTypeMethod.getName())) {
            return false;
        }

        if (subTypeMethod.getParameterTypes().length != superTypeMethod.getParameterTypes().length) {
            return false;
        }

        if (!superTypeMethod.getDeclaringClass().isAssignableFrom(subTypeMethod.getDeclaringClass())) {
            return false;
        }

        return parametersResolveToSameTypes(subTypeMethod, superTypeMethod);
    }

    /**
     * Taken from Hibernate Validator
     */
    protected boolean parametersResolveToSameTypes(Method subTypeMethod, Method superTypeMethod) {
        if (subTypeMethod.getParameterTypes().length == 0) {
            return true;
        }

        ResolvedType resolvedSubType = typeResolver.resolve(subTypeMethod.getDeclaringClass());
        MemberResolver memberResolver = new MemberResolver(typeResolver);
        memberResolver.setMethodFilter(new SimpleMethodFilter(subTypeMethod, superTypeMethod));
        ResolvedTypeWithMembers typeWithMembers = memberResolver.resolve(resolvedSubType, null, null);
        ResolvedMethod[] resolvedMethods = typeWithMembers.getMemberMethods();

        // The ClassMate doc says that overridden methods are flattened to one
        // resolved method. But that is the case only for methods without any
        // generic parameters.
        if (resolvedMethods.length == 1) {
            return true;
        }

        // For methods with generic parameters I have to compare the argument
        // types (which are resolved) of the two filtered member methods.
        for (int i = 0; i < resolvedMethods[0].getArgumentCount(); i++) {

            if (!resolvedMethods[0].getArgumentType(i).equals(resolvedMethods[1].getArgumentType(i))) {
                return false;
            }
        }

        return true;
    }

    protected Validator getValidator(HttpRequest request) {
        Locale locale = getLocale(request);
        if (locale == null) {
            return validatorFactory.getValidator();
        }

        MessageInterpolator interpolator = new LocaleSpecificMessageInterpolator(validatorFactory.getMessageInterpolator(), locale);
        return validatorFactory.usingContext().messageInterpolator(interpolator).getValidator();
    }

    protected SimpleViolationsContainer getViolationsContainer(HttpRequest request, Object target) {
        if (request == null) {
            return new SimpleViolationsContainer(target);
        }

        SimpleViolationsContainer violationsContainer = SimpleViolationsContainer.class.cast(request.getAttribute(SimpleViolationsContainer.class.getName()));
        if (violationsContainer == null) {
            violationsContainer = new SimpleViolationsContainer(target);
            request.setAttribute(SimpleViolationsContainer.class.getName(), violationsContainer);
        }
        return violationsContainer;
    }

    private Locale getLocale(HttpRequest request) {
        if (request == null) {
            return null;
        }
        List<Locale> locales = request.getHttpHeaders().getAcceptableLanguages();
        return locales == null || locales.isEmpty() ? null : locales.get(0);
    }

    /**
     * A filter implementation filtering methods matching given methods.
     *
     * @author Gunnar Morling
     *         <p>
     *         Taken from Hibernate Validator
     */
    protected static class SimpleMethodFilter implements Filter<RawMethod> {
        private final Method method1;
        private final Method method2;

        private SimpleMethodFilter(Method method1, Method method2) {
            this.method1 = method1;
            this.method2 = method2;
        }

        @Override
        public boolean include(RawMethod element) {
            return element.getRawMember().equals(method1) || element.getRawMember().equals(method2);
        }
    }

    protected static class LocaleSpecificMessageInterpolator implements MessageInterpolator {
        private final MessageInterpolator interpolator;
        private final Locale locale;

        public LocaleSpecificMessageInterpolator(MessageInterpolator interpolator, Locale locale) {
            this.interpolator = interpolator;
            this.locale = locale;
        }

        @Override
        public String interpolate(String messageTemplate, Context context) {
            return interpolator.interpolate(messageTemplate, context, locale);
        }

        @Override
        public String interpolate(String messageTemplate, Context context, Locale locale) {
            return interpolator.interpolate(messageTemplate, context, locale);
        }
    }
}
