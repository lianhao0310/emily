package com.alice.emily.module.security.jwt;

import com.alice.emily.module.security.jwt.web.JWTAuthenticationRequest;
import com.alice.emily.module.security.jwt.web.JWTAuthenticationResponse;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by lianhao on 2017/4/7.
 */
@ActiveProfiles("jwt")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractJsonWebTokenTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    protected JWTTokenService tokenService;
    @Autowired
    protected TestRestTemplate template;

    public String getToken(String username, String password) {
        JWTAuthenticationRequest request = new JWTAuthenticationRequest(username, password);
        JWTAuthenticationResponse response = template.postForObject("/auth", request, JWTAuthenticationResponse.class);
        String token = response.getToken();
        Assert.assertNotNull(token);
        System.out.println("Token: " + token);
        return token;
    }
}
