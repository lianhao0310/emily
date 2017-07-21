package com.alice.emily.module.security.jwt;

import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

/**
 * Created by lianhao on 2017/4/7.
 */
public interface JWTTokenService {

    <T> T get(String token, String claimName, Class<T> requiredType);

    String getUsernameFromToken(String token);

    Date getCreatedDateFromToken(String token);

    Date getExpirationDateFromToken(String token);

    String getAudienceFromToken(String token);

    String generateToken(UserDetails userDetails, Device device);

    String generateToken(Map<String, Object> claims);

    Boolean isTokenCanBeRefreshed(String token, UserDetails userDetails);

    String refreshToken(String token);

    Boolean isTokenExpired(String token);

    Boolean validateToken(String token, UserDetails userDetails);
}
