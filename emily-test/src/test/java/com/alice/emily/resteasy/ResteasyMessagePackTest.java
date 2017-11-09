package com.alice.emily.resteasy;

import com.alice.emily.resteasy.resource.Person;
import com.alice.emily.utils.HTTP;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Created by lianhao on 2017/3/27.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResteasyMessagePackTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private WebTestClient client;

    @Test
    public void test() {
        Person person = Person.newPerson("Nick", "3th avenue", 24);

        client.post().uri("/person/Nick")
                .accept(MediaType.valueOf(HTTP.APPLICATION_X_MSGPACK))
                .contentType(MediaType.valueOf(HTTP.APPLICATION_X_MSGPACK))
                .syncBody(person)
                .exchange()
                .expectStatus().isNoContent();

        client.get().uri("/person/Nick")
                .accept(MediaType.valueOf(HTTP.APPLICATION_X_MSGPACK))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class).isEqualTo(person);
    }

}
