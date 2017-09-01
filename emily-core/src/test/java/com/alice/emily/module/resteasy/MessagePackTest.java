package com.alice.emily.module.resteasy;

import com.alice.emily.module.resteasy.resource.Person;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by lianhao on 2017/3/27.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessagePackTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test() {
        Person person = Person.newPerson("Nick", "3th avenue", 24);
        MediaType mediaType = MediaType.valueOf("application/x-msgpack");

        // post entity to server
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(mediaType));
        headers.setContentType(mediaType);
        HttpEntity<Person> request = new HttpEntity<>(person, headers);
        ResponseEntity<Void> postResponseEntity = restTemplate.postForEntity("/person/Nick", request, Void.class);
        Assert.assertTrue(postResponseEntity.getStatusCode().is2xxSuccessful());

        // get entity
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Person> getResponseEntity = restTemplate.exchange("/person/Nick", HttpMethod.GET, httpEntity, Person.class);
        Assert.assertTrue(getResponseEntity.getStatusCode().is2xxSuccessful());
        Assert.assertEquals(getResponseEntity.getBody(), person);
    }

}
