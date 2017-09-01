package com.alice.emily.module.security.keycloak.resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class ProtectedResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @PreAuthorize("hasRole('USER')")
    public Response getGreeting(@AuthenticationPrincipal Principal principal) {
        System.out.println("Principal: " + principal);
        return Response.ok().entity("Greetings from get method!").build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @PreAuthorize("hasRole('USER')")
    public Response postGreeting(@FormParam("greet") String greeting) {
        System.out.println("Received Greeting: " + greeting);
        return Response.ok().entity("Greetings from post method!").build();
    }
}
