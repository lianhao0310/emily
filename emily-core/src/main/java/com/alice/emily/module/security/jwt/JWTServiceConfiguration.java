package com.alice.emily.module.security.jwt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.alice.emily.module.security.jwt.web.AuthenticationRestController;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lianhao on 2017/4/9.
 */
@Log4j2
@Configuration
public class JWTServiceConfiguration {

    private final JWTProperties properties;

    public JWTServiceConfiguration(JWTProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public JWTTokenService jwtTokenService() {
        if (StringUtils.isEmpty(properties.getSecret())) {
            String secret = UUID.randomUUID().toString();
            log.warn("No secret provided for emily.jwt.secret, use a random instead: {}", secret);
            properties.setSecret(secret);
        }
        return new DefaultJWTTokenService(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsService jwtUserDetailService(PasswordEncoder passwordEncoder) {
        return new EmbeddedUserDetailsService(properties, passwordEncoder);
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultJWTClaimFilter defaultJWTClaimFilter() {
        return new DefaultJWTClaimFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationRestController authenticationRestController() {
        return new AuthenticationRestController();
    }

    public static class EmbeddedUserDetailsService implements UserDetailsService {

        private final Map<String, UserDetails> userDetailsMap = Maps.newHashMap();

        EmbeddedUserDetailsService(JWTProperties properties, PasswordEncoder passwordEncoder) {
            // ensure at least one users exist
            List<JWTProperties.User> users = properties.getUsers() != null
                    ? Lists.newArrayList(properties.getUsers()) : Lists.newArrayList();
            if (users.isEmpty()) {
                users.add(new JWTProperties.User());
            }
            // generate SimpleUserDetails based on users
            for (JWTProperties.User user : users) {
                UserDetails details = User.withUsername(user.getUsername())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .authorities(user.getAuthorities())
                        .disabled(!user.isEnabled())
                        .build();
                userDetailsMap.put(user.getUsername(), details);
            }
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserDetails userDetails = userDetailsMap.get(username);
            if (userDetails != null) {
                // when current request complete, Spring Security will erase the credentials
                // attached to current User. In case it affected the map we hold in the memory,
                // We use a clone of the original User.
                return User.withUsername(userDetails.getUsername())
                        .password(userDetails.getPassword())
                        .authorities(userDetails.getAuthorities()
                                .toArray(new GrantedAuthority[userDetails.getAuthorities().size()]))
                        .disabled(!userDetails.isEnabled())
                        .build();
            }
            throw new UsernameNotFoundException(username);
        }
    }
}
