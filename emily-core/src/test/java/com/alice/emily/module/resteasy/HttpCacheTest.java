package com.palmaplus.euphoria.module.resteasy;

import com.google.common.collect.Lists;
import com.palmaplus.euphoria.module.resteasy.resource.Person;
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
 * Created by liupin on 2017/3/27.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpCacheTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void test() {
        // post entity to server
        Person person = Person.newPerson("Jone", "3th avenue", 35);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Person> request = new HttpEntity<>(person, headers);
        ResponseEntity<Void> postResponse = template.postForEntity("/person/Jone", request, Void.class);
        Assert.assertTrue(postResponse.getStatusCode().is2xxSuccessful());

        // try get entity
        ResponseEntity<Person> getResponse1 = template.getForEntity("/person/Jone", Person.class);
        Assert.assertTrue(getResponse1.getStatusCode().is2xxSuccessful());
        HttpHeaders getResponse1Headers = getResponse1.getHeaders();
        Assert.assertTrue(getResponse1.getStatusCode().is2xxSuccessful());
        Assert.assertNotNull(getResponse1Headers.getETag());
        Assert.assertEquals(person, getResponse1.getBody());

        // try get entity again
        headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setIfNoneMatch(getResponse1Headers.getETag());

        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Void> getResponse2 = template.exchange("/person/Jone", HttpMethod.GET, entity, Void.class);
        Assert.assertTrue(getResponse2.getStatusCode().is3xxRedirection());
    }

}
