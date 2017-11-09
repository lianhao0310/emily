package com.alice.emily.resteasy.resource;

import com.alice.emily.utils.HTTP;
import org.jboss.resteasy.annotations.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by lianhao on 2017/3/27.
 */
@Component
@Path("/person")
@Produces({ MediaType.APPLICATION_JSON, HTTP.APPLICATION_X_MSGPACK })
public class PersonResource {

    @Autowired
    private PersonService personService;

    @Cache(maxAge = 1000)
    @GET
    @Path("{name:\\w+}")
    public Person getPerson(@PathParam("name") @NotNull String name) {
        return personService.getPerson(name);
    }

    @POST
    @Path("{name:\\w+}")
    @Consumes({ MediaType.APPLICATION_JSON, HTTP.APPLICATION_X_MSGPACK })
    public void post(@PathParam("name") String name, @Valid Person person) {
        System.out.println("request param: name=" + name);
        System.out.println("request body: " + person);
        personService.putPerson(name, person);
    }
}
