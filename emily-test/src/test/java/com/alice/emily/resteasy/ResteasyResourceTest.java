package com.alice.emily.resteasy;

import com.alice.emily.resteasy.resource.Person;
import com.alice.emily.test.web.TestHttp;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

/**
 * Created by wyx on 12/23/15.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ResteasyResourceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private WebTestClient client;

    @Test
    public void testGet() throws IOException {
        client.get().uri("/")
                .exchange()
                .expectBody(String.class).isEqualTo("1");
    }

    @Test
    public void testPost() {
        client.post().uri("/person/Nick")
                .accept(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.fromCallable(() -> Person.newPerson("Nick", "3th avenue", 24)), Person.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void testOption() {
        String origin = "http://localhost:8080";
        client.options().uri("/")
                .accept(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.PATCH.name())
                .header(HttpHeaders.ORIGIN, origin)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, HttpMethod.PATCH.name())
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
    }

    @Test
    public void testValidator() {
        client.post().uri("/person/Tom")
                .accept(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.fromCallable(() -> Person.newPerson("Tom", "", 101)), Person.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testPageable() {
        client.get().uri("/photo")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .consumeWith(result -> {
                    Map page = result.getResponseBody();
                    Assert.assertNotNull(page);
                    Assert.assertEquals(page.get("numberOfElements"), 20);
                });

        client.get().uri("/photo?size=100&page=2&sort=name,DESC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .consumeWith(result -> {
                    Map page = result.getResponseBody();
                    Assert.assertNotNull(page);
                    Assert.assertEquals(page.get("numberOfElements"), 100);
                    Assert.assertEquals(page.get("number"), 2);
                });
    }

    @Test
    public void testFileUpload() throws IOException {
        ClassPathResource resource = new ClassPathResource("application.properties");
        int code = TestHttp.post("/photo")
                .accept(MediaType.TEXT_PLAIN_VALUE)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .part("file", resource.getFilename(),
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        resource.getInputStream())
                .code();
        Assert.assertEquals(code, HttpResponseCodes.SC_OK);

        code = TestHttp.post("/photo/part")
                .accept(MediaType.TEXT_PLAIN_VALUE)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .part("file", resource.getFilename(),
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        resource.getInputStream())
                .code();
        Assert.assertEquals(code, HttpResponseCodes.SC_OK);
    }

    @Test
    public void testFileUploadByMVC() throws IOException {
        ClassPathResource resource = new ClassPathResource("application.properties");
        int code = TestHttp.post("/photo/mvc?name=test")
                .accept(MediaType.TEXT_PLAIN_VALUE)
                .part("desc", "test resource file")
                .part("version", 5)
                .part("file", resource.getFilename(),
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        resource.getInputStream())
                .code();
        Assert.assertEquals(code, HttpResponseCodes.SC_OK);
    }
}