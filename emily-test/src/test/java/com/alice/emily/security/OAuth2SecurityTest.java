package com.alice.emily.security;

import com.alice.emily.security.oauth2.OAuth2Configuration;
import com.alice.emily.security.oauth2.WebSecurityConfiguration;
import com.alice.emily.test.web.TestHttp;
import com.alice.emily.utils.JSON;
import org.junit.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.http.MediaType;

import java.util.Map;

/**
 * Created by lianhao on 2017/7/31.
 */
@ImportAutoConfiguration({ OAuth2AutoConfiguration.class,
        OAuth2Configuration.class, WebSecurityConfiguration.class })
public class OAuth2SecurityTest extends AbstractSecurityTest {

    private String getToken() {
        String body = TestHttp.post("/oauth/token")
                .acceptJson()
//                .basic("webapp", "secret")
                .form("username", "user")
                .form("password", "password")
                .form("grant_type", "password")
                .form("scope", "read write")
                .form("client_id", "webapp")
                .form("client_secret", "secret")
                .body();
        Map<String, Object> map = JSON.readObject(body);
        return map.get("token_type") + " " + map.get("access_token");
    }


    @Test
    public void testAccessProtected() {
        String token = getToken();

        client.get().uri(PROTECTED_CONTROLLER_USER)
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class).isEqualTo("Greetings from user get controller method!");

        client.get().uri(PROTECTED_RESOURCE_TEST)
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

}
