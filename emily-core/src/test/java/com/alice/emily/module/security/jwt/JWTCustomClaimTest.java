package com.alice.emily.module.security.jwt;

import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Map;

/**
 * Created by lianhao on 2017/4/7.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "test.custom.claim=true")
public class JWTCustomClaimTest extends AbstractJsonWebTokenTest {

    @Test
    public void testCustomClaim() {
        String token = getToken("emily", "password");
        String username = tokenService.getUsernameFromToken(token);
        Assertions.assertThat(username).isEqualTo("emily");

        Date createDate = tokenService.get(token, "createDate", Date.class);
        Assertions.assertThat(createDate).isNotNull().isBefore(new Date());

        String address = tokenService.get(token, "address", String.class);
        Assertions.assertThat(address).isNotBlank().isEqualTo(CustomClaimConfiguration.ADDRESS);

        String test = tokenService.get(token, "test", String.class);
        Assertions.assertThat(test).isNotBlank().isEqualTo(CustomClaimConfiguration.TEST_CLAIM_VALUE);
    }

    @Test
    public void testClaimFilter() {
        String token = getToken("admin", "password");
        String username = tokenService.getUsernameFromToken(token);
        Assertions.assertThat(username).isEqualTo("admin");

        Date createDate = tokenService.get(token, "createDate", Date.class);
        Assertions.assertThat(createDate).isNotNull().isAfter(new Date());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", token);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> entity = template.exchange("/protected-controller", HttpMethod.GET, httpEntity, Map.class);
        Assert.assertEquals(401, entity.getStatusCodeValue());
    }
}
