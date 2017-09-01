package com.palmaplus.euphoria.module.security.keycloak;

import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * Created by liupin on 2017/4/5.
 */
@ActiveProfiles("keycloak")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KeycloakTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testAdmin() {
        // pagination
        System.out.println("Pagination: ");
        List<UserRepresentation> userRepresentations = keycloak.realm("master").users().search("", 0, 10);
        userRepresentations.forEach(user -> System.out.println(user.getUsername()));
        System.out.println();

        // search
        System.out.println("Search: ");
        userRepresentations = keycloak.realm("master").users().search("sifan", 0, 10);
        userRepresentations.forEach(user -> System.out.println(user.getUsername()));
        System.out.println();

        // count
        Integer count = keycloak.realm("master").users().count();
        System.out.println("Users' count: " + count);
    }

    @Test
    public void testAccessResource() {
        ResponseEntity<String> entity = template.getForEntity("/protected/resource", String.class);
        Assertions.assertThat(entity.getStatusCode().is3xxRedirection()).isTrue();

        String token = keycloak.tokenManager().getAccessTokenString();
        System.out.println("Token: " + token);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity getRequest = new HttpEntity(headers);
        ResponseEntity<String> response = template.exchange("/protected/resource", HttpMethod.GET, getRequest, String.class);
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        System.out.println("Get: " + response.getBody());

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Lists.newArrayList(MediaType.TEXT_PLAIN));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("greet", "Test post resource");
        HttpEntity<MultiValueMap<String, String>> postRequest = new HttpEntity<>(params, headers);
        response = template.exchange("/protected/resource", HttpMethod.POST, postRequest, String.class);
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        System.out.println("Post: " + response.getBody());
    }

    @Test
    public void testAccessController() {
        ResponseEntity<String> entity = template.getForEntity("/protected/controller", String.class);
        Assertions.assertThat(entity.getStatusCode().is3xxRedirection()).isTrue();

        String token = keycloak.tokenManager().getAccessTokenString();
        System.out.println("Token: " + token);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity getRequest = new HttpEntity(headers);
        ResponseEntity<String> response = template.exchange("/protected/controller", HttpMethod.GET, getRequest, String.class);
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        System.out.println("Get: " + response.getBody());

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Lists.newArrayList(MediaType.TEXT_PLAIN));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("greet", "Test post resource");
        HttpEntity<MultiValueMap<String, String>> postRequest = new HttpEntity<>(params, headers);
        response = template.exchange("/protected/resource", HttpMethod.POST, postRequest, String.class);
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        System.out.println("Post: " + response.getBody());
    }
}
