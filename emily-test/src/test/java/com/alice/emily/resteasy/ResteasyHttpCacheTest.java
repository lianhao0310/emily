package com.alice.emily.resteasy;

import com.alice.emily.resteasy.resource.Person;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Created by lianhao on 2017/3/27.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "emily.resteasy.http-cache=true", "emily.web.shallow-etag.enabled=false" })
public class ResteasyHttpCacheTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private WebTestClient client;

    @Test
    public void test() {
        // post entity to server
        Person person = Person.newPerson("Jone", "3th avenue", 35);
        client.post().uri("/person/Jone")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(person)
                .exchange()
                .expectStatus().isNoContent();

        // try get entity
        HttpHeaders headers = client.get().uri("/person/Jone")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueMatches(HttpHeaders.ETAG, "(W/\")?[\\w-]+(\")?")
                .expectBody(Person.class).isEqualTo(person)
                .returnResult()
                .getResponseHeaders();

        // try get entity again
        client.get().uri("/person/Jone")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_NONE_MATCH, headers.getETag())
                .exchange()
                .expectStatus().isNotModified();
    }

}
