package com.palmaplus.euphoria.module.resteasy;

import com.palmaplus.euphoria.module.resteasy.resource.Person;
import org.assertj.core.api.Assertions;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * Created by liupin on 2017/4/6.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResteasyClientTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private ResteasyClient client;

    @LocalServerPort
    private int port;

    @Test
    public void test() {
        Person person = Person.newPerson("Nick", "3th avenue", 24);
        int status = client.target("http://localhost:" + port + "/person/Nick")
                .request()
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
                .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE))
                .getStatus();
        Assertions.assertThat(status).isEqualTo(204);

        Person entity = client.target("http://localhost:" + port + "/person/Nick")
                .request()
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
                .get()
                .readEntity(Person.class);
        Assertions.assertThat(entity)
                .isNotNull()
                .isEqualToComparingFieldByField(person);
    }
}
