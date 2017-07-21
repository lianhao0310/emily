package com.alice.emily.autoconfigure;

import com.alice.emily.module.security.jwt.JWTProperties;
import com.alice.emily.module.security.jwt.JWTSecurityConfiguration;
import com.alice.emily.module.security.jwt.JWTServiceConfiguration;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServlet;

/**
 * Created by liupin on 2017/3/15.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ HttpServlet.class, WebSecurityConfigurerAdapter.class, Jwts.class })
@ConditionalOnProperty(prefix = "emily.jwt", name = "authPath")
@AutoConfigureBefore(SecurityExtAutoConfiguration.class)
@Import({ JWTServiceConfiguration.class, JWTSecurityConfiguration.class })
@EnableConfigurationProperties(JWTProperties.class)
public class JWTAutoConfiguration {

}
