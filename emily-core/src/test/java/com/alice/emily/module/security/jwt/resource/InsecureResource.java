package com.palmaplus.euphoria.module.security.jwt.resource;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by liupin on 2017/4/7.
 */
@Component
@Path("/insecure")
@Produces(MediaType.TEXT_PLAIN)
public class InsecureResource {

    @GET
    public String get() {
        return "Welcome access the insecure resource";
    }
}
