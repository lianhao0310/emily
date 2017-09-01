package com.alice.emily.example.okhttp.controller;

import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by Lianhao on 2017/7/25.
 */
@Component
@Path("default")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DefaultController {
    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id){
        return Response.ok().entity(id).build();
    }

    @POST
    @Path("/post")
    public Response post(String data){
        return Response.ok().entity(data).build();
    }

    @POST
    @Path("/postMap")
    public Response postMap(Map<String, Object> data){
        return Response.ok().entity(data).build();
    }
}
