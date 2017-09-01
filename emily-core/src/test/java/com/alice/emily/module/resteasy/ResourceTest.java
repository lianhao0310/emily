package com.palmaplus.euphoria.module.resteasy;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Lists;
import com.palmaplus.euphoria.module.resteasy.resource.Person;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;

/**
 * Created by wyx on 12/23/15.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ResourceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void testGet() throws IOException {
        String body = restTemplate.getForObject("/", String.class);
        Assert.assertEquals("1", body);
    }

    @Test
    public void testPost() {
        Person person = Person.newPerson("Nick", "3th avenue", 24);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Person> request = new HttpEntity<>(person, headers);

        ResponseEntity<Void> entity = restTemplate.postForEntity("/person/Nick", request, Void.class);
        Assert.assertTrue(entity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testOption() {
        String origin = "http://localhost:8080";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setOrigin(origin);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("/", HttpMethod.OPTIONS, request, Void.class);
        Assert.assertEquals(response.getHeaders().getAccessControlAllowMethods().size(), 6);
        for (HttpMethod httpMethod : response.getHeaders().getAccessControlAllowMethods()) {
            Assert.assertTrue(response.getHeaders().getAllow().contains(httpMethod));
        }
        Assert.assertEquals(response.getHeaders().getAccessControlAllowOrigin(), origin);
    }

    @Test
    public void testValidator() {
        Person person = Person.newPerson("Tom", "", 101);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Person> request = new HttpEntity<>(person, headers);

        ResponseEntity<Void> postPersonResponse = restTemplate.postForEntity("/person/Tom", request, Void.class);
        Assert.assertTrue(postPersonResponse.getStatusCode().is4xxClientError());
    }

    @Test
    public void testFileUpload() {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        HttpHeaders partHeaders = new HttpHeaders();
        partHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        HttpEntity<Resource> partEntity = new HttpEntity<>(new ClassPathResource("application.properties"), partHeaders);
        map.add("file", partEntity);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> result = restTemplate.exchange("/photo", HttpMethod.POST, request, String.class);
        Assert.assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testFileUploadByMVC() throws IOException {
        ClassPathResource resource = new ClassPathResource("application.properties");
        int code = HttpRequest.post(String.format("http://localhost:%d/photo/mvc", port))
                .accept(MediaType.TEXT_PLAIN_VALUE)
                .part("desc", "test resource file")
                .part("file", resource.getFilename(), MediaType.TEXT_PLAIN_VALUE, resource.getInputStream())
                .code();
        Assert.assertEquals(code, HttpResponseCodes.SC_OK);
    }

}