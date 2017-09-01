package com.alice.emily.module.security.jwt.resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by lianhao on 2017/4/5.
 */
@Component
@Path("/protected-resource")
public class ProtectedResource {

    @GET
    @PreAuthorize("hasRole('TEST')")
    public Response getProtectedGreeting(@AuthenticationPrincipal User principal) {
        System.out.println("Principal: " + principal);
        return Response.ok().entity("Greetings from test protected method!").build();
    }
}
