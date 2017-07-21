package com.alice.emily.module.security.principal;

import org.jboss.resteasy.util.FindAnnotation;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by lianhao on 2017/4/5.
 */
@Provider
@Consumes("*/*")
public class AuthenticationPrincipalReader implements MessageBodyReader<Object> {

    private ExpressionParser parser = new SpelExpressionParser();

    private final BeanResolver beanResolver;

    public AuthenticationPrincipalReader(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return FindAnnotation.findAnnotation(annotations, AuthenticationPrincipal.class) != null;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations,
                           MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                           InputStream entityStream) throws IOException, WebApplicationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        AuthenticationPrincipal authPrincipal = FindAnnotation.findAnnotation(annotations, AuthenticationPrincipal.class);
        String expressionToParse = authPrincipal.expression();
        if (StringUtils.hasLength(expressionToParse)) {
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setRootObject(principal);
            context.setVariable("this", principal);
            context.setBeanResolver(beanResolver);

            Expression expression = this.parser.parseExpression(expressionToParse);
            principal = expression.getValue(context);
        }
        if (principal != null && !type.isAssignableFrom(principal.getClass())) {
            if (authPrincipal.errorOnInvalidType()) {
                throw new ClassCastException(principal + " is not assignable to " + type);
            } else {
                return null;
            }
        }
        return principal;
    }
}
