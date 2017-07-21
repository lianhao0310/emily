package com.alice.emily.module.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

/**
 * Created by lianhao on 2017/4/7.
 */
public interface JWTClaimFilter {

    void generateClaims(UserDetails userDetails, Map<String, Object> claims);

    boolean isClaimsValid(UserDetails userDetails, Map<String, Object> claims);
}