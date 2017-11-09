package com.alice.emily.resteasy;

import com.alice.emily.resteasy.resource.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jboss.resteasy.annotations.Form;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by lianhao on 2017/4/6.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResteasyClientTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private ResteasyClient client;

    @LocalServerPort
    private int port;

    @Test
    public void testDirectUse() {
        Person person = Person.newPerson("Nick", "3th avenue", 24);
        int status = client.target("http://localhost:" + port + "/person/Nick")
                .request()
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
                .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE))
                .getStatus();
        assertThat(status).isEqualTo(204);

        Person entity = client.target("http://localhost:" + port + "/person/Nick")
                .request()
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
                .get()
                .readEntity(Person.class);
        assertThat(entity)
                .isNotNull()
                .isEqualToComparingFieldByField(person);
    }

    @Test
    public void testProxy() {
        HttpBinService binService = client.target("http://httpbin.org").proxy(HttpBinService.class);
        HttpBinResponse response = binService.postWithJson(new LoginData("username", "secret"));
        printResponse(response);

        response = binService.postWithFormParams("emily");
        printResponse(response);

        response = binService.get();
        printResponse(response);

        response = binService.getWithArg("retrofit");
        printResponse(response);
    }

    private void printResponse(HttpBinResponse response) {
        System.out.println("Response (contains request infos):");
        System.out.println("- url:         " + response.url);
        System.out.println("- ip:          " + response.origin);
        System.out.println("- headers:     " + response.headers);
        System.out.println("- args:        " + response.args);
        System.out.println("- form params: " + response.form);
        System.out.println("- json params: " + response.json);
    }

    /**
     * Generic HttpBin.org Response Container
     */
    @Data
    public static class HttpBinResponse {
        private String url;           // the request url
        private String origin;        // the requester ip
        private Map headers;          // all headers that have been sent
        private Map args;             // url arguments
        private Map form;             // post form parameters
        private Map json;             // post body json
    }

    /**
     * Exemplary login data sent as JSON
     */
    @Data
    @AllArgsConstructor
    public static class LoginData {
        private String username;
        private String password;
    }

    /**
     * HttpBin.org service definition
     */
    @Consumes(MediaType.APPLICATION_JSON)
    public interface HttpBinService {

        @GET
        @Path("get")
        HttpBinResponse get();

        // request /get?testArg=...
        @GET
        @Path("get")
        HttpBinResponse getWithArg(@QueryParam("testArg") String arg);

        // POST form encoded with form field params
        @POST
        @Path("post")
        HttpBinResponse postWithFormParams(@FormParam("field1") String field1);

        // POST form encoded with form field params
        @POST
        @Path("post")
        HttpBinResponse postWithJson(@Form LoginData loginData);
    }
}
