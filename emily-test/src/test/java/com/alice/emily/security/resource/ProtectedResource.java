package com.alice.emily.security.resource;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;

/**
 * Created by lianhao on 2017/4/5.
 */
@Component
@Path("/protected/resource")
@Produces({ "text/plain;charset=UTF-8", MediaType.APPLICATION_JSON })
public class ProtectedResource {

    @GET
    @Path("test")
    public Response getGreeting(@AuthenticationPrincipal UserDetails principal) {
        System.out.println("Principal: " + principal);
        return Response.ok().entity("Greetings from test get resource method!").build();
    }

    @GET
    @Path("user")
    public Response getGreeting(@AuthenticationPrincipal Principal principal) {
        System.out.println("Principal: " + principal);
        return Response.ok().entity("Greetings from user get resource method!").build();
    }

    @POST
    @Path("user")
    public Response postGreeting(@FormParam("greet") String greeting) {
        System.out.println("Received Greeting: " + greeting);
        return Response.ok().entity("Greetings from user post resource method!").build();
    }

    @GET
    @Path("/constraint/admin")
    public Response getAdminGreeting() {
        return Response.ok().entity("Greetings from constraint admin get resource method!").build();
    }

    @POST
    @Path("/constraint/admin")
    public Response postAdminGreeting(@FormParam("greet") String greeting) {
        System.out.println("Received Greeting: " + greeting);
        return Response.ok().entity("Greetings from constraint admin post resource method!").build();
    }
}
