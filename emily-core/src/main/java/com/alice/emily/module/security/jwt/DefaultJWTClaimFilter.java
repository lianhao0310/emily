package com.alice.emily.module.security.jwt;

import com.google.common.collect.Maps;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Created by lianhao on 2017/4/7.
 */
public class DefaultJWTClaimFilter implements JWTClaimFilter {

    @Override
    public void generateClaims(UserDetails userDetails, Map<String, Object> claims) {
        claims.putAll(getClaimsFromUserDetails(userDetails));
    }

    @Override
    public boolean isClaimsValid(UserDetails userDetails, Map<String, Object> claims) {
        return true;
    }

    private Map<String, Object> getClaimsFromUserDetails(UserDetails userDetails) {
        Map<String, Object> claims = Maps.newHashMap();

        // getter method
        ReflectionUtils.doWithMethods(userDetails.getClass(), method -> {
            if (Modifier.isPublic(method.getModifiers())
                    && method.getName().startsWith("get")
                    && method.getParameterCount() == 0
                    && method.isAnnotationPresent(Claim.class)) {
                Claim claim = method.getAnnotation(Claim.class);
                String name = StringUtils.hasLength(claim.value()) ? claim.value()
                        : StringUtils.uncapitalize(method.getName().substring(3));
                try {
                    Object value = method.invoke(userDetails);
                    if (value != null) {
                        claims.put(name, value);
                    }
                } catch (InvocationTargetException ignore) {
                }
            }
        });

        // field
        ReflectionUtils.doWithFields(userDetails.getClass(), field -> {
            if (field.isAnnotationPresent(Claim.class)) {
                ReflectionUtils.makeAccessible(field);
                Claim claim = field.getAnnotation(Claim.class);
                String name = StringUtils.hasLength(claim.value()) ? claim.value() : field.getName();
                // skip process if @Claim already on getter method
                if (!claims.containsKey(name)) {
                    Object value = field.get(userDetails);
                    if (value != null) {
                        claims.put(name, value);
                    }
                }
            }
        });

        return claims;
    }
}
