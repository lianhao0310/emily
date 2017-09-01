package com.palmaplus.euphoria.module.resteasy.resource;

import lombok.NonNull;
import org.jboss.resteasy.annotations.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by liupin on 2017/3/27.
 */
@Component
@Path("/person")
@Consumes({ MediaType.APPLICATION_JSON, "application/x-msgpack" })
@Produces({ MediaType.APPLICATION_JSON, "application/x-msgpack" })
public class PersonResource {

    @Autowired
    private PersonService personService;

    @Cache(maxAge = 1000)
    @GET
    @Path("{name:\\w+}")
    public Person getPerson(@NonNull @PathParam("name") String name) {
        return personService.getPerson(name);
    }

    @POST
    @Path("{name:\\w+}")
    public void post(@PathParam("name") String name, @Valid Person person) {
        System.out.println("request param: name=" + name);
        System.out.println("request body: " + person);
        personService.putPerson(name, person);
    }
}
