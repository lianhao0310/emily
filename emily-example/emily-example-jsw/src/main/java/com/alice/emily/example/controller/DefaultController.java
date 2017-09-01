package com.alice.emily.example.controller;

import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Lianhao on 2017/7/28.
 */
@Component
@Path("default")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DefaultController {
    @GET
    @Path("/index")
    public String index() {
        return "hello";
    }
}
