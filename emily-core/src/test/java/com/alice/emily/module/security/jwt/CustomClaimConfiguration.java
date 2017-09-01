package com.palmaplus.euphoria.module.security.jwt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.palmaplus.euphoria.module.security.principal.SimpleUserDetails;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liupin on 2017/4/7.
 */
@Configuration
@ConditionalOnProperty(prefix = "test.custom", name = "claim", havingValue = "true")
public class CustomClaimConfiguration {

    public static final String ADDRESS = "First venue";
    public static final String TEST_CLAIM_VALUE = "Test claim value";

    private final JWTProperties properties;

    public CustomClaimConfiguration(JWTProperties properties) {
        this.properties = properties;
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        Map<String, CustomClaimUserDetails> userDetails = Maps.newHashMap();
        // ensure at least one users exist
        List<JWTProperties.User> users = properties.getUsers() != null
                ? Lists.newArrayList(properties.getUsers()) : Lists.newArrayList();
        if (users.isEmpty()) {
            users.add(new JWTProperties.User());
        }
        // generate CustomClaimUserDetails based on users
        for (JWTProperties.User user : users) {
            CustomClaimUserDetails customClaimUserDetails = new CustomClaimUserDetails(user, passwordEncoder);
            customClaimUserDetails.setAddress(ADDRESS);
            // make admin not pass claim filter check
            if ("admin".equals(user.getUsername())) {
                customClaimUserDetails.setCreateDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
            }
            userDetails.put(user.getUsername(), customClaimUserDetails);
        }
        return username -> {
            CustomClaimUserDetails userDetail = userDetails.get(username);
            if (userDetail != null) return new CustomClaimUserDetails(userDetail);
            throw new UsernameNotFoundException(username);
        };
    }

    @Bean
    public JWTClaimFilter jwtClaimFilter() {
        return new JWTClaimFilter() {
            @Override
            public void generateClaims(UserDetails userDetails, Map<String, Object> claims) {
                claims.put("test", TEST_CLAIM_VALUE);
            }

            @Override
            public boolean isClaimsValid(UserDetails userDetails, Map<String, Object> claims) {
                Long time = (Long) claims.get("createDate");
                return time < System.currentTimeMillis();
            }
        };
    }

    @ToString
    @EqualsAndHashCode(callSuper = true)
    public static class CustomClaimUserDetails extends SimpleUserDetails {

        @Claim
        private Date createDate = new Date();

        private String address;

        public CustomClaimUserDetails(JWTProperties.User user, PasswordEncoder encoder) {
            this.username = user.getUsername();
            this.password = encoder.encode(user.getPassword());
            this.authorities = user.getRoles().stream()
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            this.enabled = user.isEnabled();
        }

        public CustomClaimUserDetails(CustomClaimUserDetails userDetails) {
            this.username = userDetails.getUsername();
            this.password = userDetails.getPassword();
            this.authorities = userDetails.getAuthorities();
            this.enabled = userDetails.isEnabled();
            this.createDate = userDetails.getCreateDate();
            this.address = userDetails.getAddress();
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

        @Claim
        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

}
