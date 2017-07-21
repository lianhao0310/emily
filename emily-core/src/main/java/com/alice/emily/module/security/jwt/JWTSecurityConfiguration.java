package com.alice.emily.module.security.jwt;

import com.alice.emily.module.security.SecurityExtConfigurer;
import com.alice.emily.module.security.jwt.web.JWTAuthenticationEntryPoint;
import com.alice.emily.module.security.jwt.web.JWTAuthenticationTokenFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Order(1000)
@Configuration
public class JWTSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JWTTokenService jwtTokenService;
    private final JWTProperties properties;
    private final PasswordEncoder passwordEncoder;
    private final ObjectProvider<SecurityExtConfigurer> configurer;

    public JWTSecurityConfiguration(UserDetailsService userDetailsService,
                                    JWTTokenService jwtTokenService,
                                    JWTProperties properties,
                                    PasswordEncoder passwordEncoder,
                                    ObjectProvider<SecurityExtConfigurer> configurer) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.properties = properties;
        this.configurer = configurer;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JWTAuthenticationEntryPoint();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        super.configure(authenticationManagerBuilder);
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint()).and()
                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(properties.getAuthPath()).permitAll();

        // Custom JWT based security filter
        JWTAuthenticationTokenFilter tokenFilter = new JWTAuthenticationTokenFilter(userDetailsService, jwtTokenService, properties);
        httpSecurity
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl().disable();

        SecurityExtConfigurer configurer = this.configurer.getIfAvailable();
        if (configurer != null) {
            configurer.configure(httpSecurity);
        }

        httpSecurity.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        SecurityExtConfigurer configurer = this.configurer.getIfAvailable();
        if (configurer != null) {
            configurer.configure(webSecurity);
        }
    }
}