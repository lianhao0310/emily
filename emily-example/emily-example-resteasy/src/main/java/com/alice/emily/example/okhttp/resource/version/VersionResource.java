package com.alice.emily.example.okhttp.resource.version;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Lianhao on 2017/8/16.
 */
@Path("/version")
@Component
public class VersionResource {
    @GET
    @Path("/getVersion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVersion(@HeaderParam("version") String version, @CookieParam("name") String name){
        if(null == version){
            return Response.status(Response.Status.CONFLICT).entity(name).build();
        }
        return Response.ok(name + ":" + version).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";version:1.0")
    public Response version1(@Context HttpServletRequest request, @Context HttpServletResponse response){
        request.getHeader("Accept");
        response.getHeader("Accept");
        return Response.ok("version1").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON + ";version:2.0"})
    public Response version2(@Context HttpServletRequest request, @Context HttpServletResponse response){
        request.getHeader("Accept");
        response.getHeader("Accept");

        return Response.ok("version2").build();
    }

    @GET
    @Path("/{name}.xml")
    @Produces("application/xml")
    public Response version3(@PathParam("name") String name){
        return Response.ok("version3").build();
    }
}
