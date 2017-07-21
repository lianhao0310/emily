package com.alice.emily.module.security.jwt;

import com.google.common.collect.Lists;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultJWTTokenService implements JWTTokenService {

    private final JWTProperties properties;

    @Autowired(required = false)
    private Collection<JWTClaimFilter> claimFilters = Lists.newArrayList();

    public DefaultJWTTokenService(JWTProperties properties) {
        this.properties = properties;
    }

    @Override
    public <T> T get(String token, String claimName, Class<T> requiredType) {
        T value;
        try {
            final Claims claims = getClaimsFromToken(token);
            value = claims.get(claimName, requiredType);
        } catch (Exception e) {
            value = null;
        }
        return value;
    }

    @Override
    public String getUsernameFromToken(String token) {
        return get(token, Claims.SUBJECT, String.class);
    }

    @Override
    public Date getCreatedDateFromToken(String token) {
        return get(token, Claims.ISSUED_AT, Date.class);
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        return get(token, Claims.EXPIRATION, Date.class);
    }

    @Override
    public String getAudienceFromToken(String token) {
        return get(token, Claims.AUDIENCE, String.class);
    }

    @Override
    public String generateToken(UserDetails userDetails, Device device) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.SUBJECT, userDetails.getUsername());
        claims.put(Claims.AUDIENCE, generateAudience(device));
        claims.put(Claims.EXPIRATION, new Date());
        for (JWTClaimFilter claimFilter : claimFilters) {
            claimFilter.generateClaims(userDetails, claims);
        }
        return generateToken(claims);
    }

    @Override
    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(properties.getSignatureAlgorithm(), properties.getSecret())
                .compact();
    }

    @Override
    public Boolean isTokenCanBeRefreshed(String token, UserDetails userDetails) {
        Claims claims = getClaimsFromToken(token);
        return (!isTokenExpired(token) || isTokenExpirationIgnored(token)) && isClaimValid(userDetails, claims);
    }

    @Override
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(Claims.ISSUED_AT, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    @Override
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    @Override
    public Boolean validateToken(String token, UserDetails user) {
        Claims claims = getClaimsFromToken(token);
        return user.getUsername().equals(claims.getSubject())
                && !claims.getExpiration().before(new Date())
                && isClaimValid(user, claims);
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(properties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private boolean isClaimValid(UserDetails user, Claims claims) {
        boolean valid = true;
        for (JWTClaimFilter claimFilter : claimFilters) {
            if (!claimFilter.isClaimsValid(user, claims)) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    private Boolean isTokenExpirationIgnored(String token) {
        String audience = getAudienceFromToken(token);
        return (JWTConstants.AUDIENCE_TABLET.equals(audience) || JWTConstants.AUDIENCE_MOBILE.equals(audience));
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + properties.getExpiration() * 1000);
    }

    private String generateAudience(Device device) {
        String audience = JWTConstants.AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = JWTConstants.AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = JWTConstants.AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = JWTConstants.AUDIENCE_MOBILE;
        }
        return audience;
    }
}