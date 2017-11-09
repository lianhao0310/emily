package com.alice.emily.resteasy.resource;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Component
@Path("/")
@Produces("text/plain")
public class TestResource {

    @GET
    public String test() {
        return "1";
    }
}