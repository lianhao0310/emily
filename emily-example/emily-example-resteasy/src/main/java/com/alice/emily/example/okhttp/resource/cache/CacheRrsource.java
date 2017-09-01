package com.alice.emily.example.okhttp.resource.cache;

import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.util.Date;


/**
 * Created by Lianhao on 2017/8/16.
 */
@Path("/cache")
@Component
public class CacheRrsource {
    private static String cust = "1";

    @Path("/{id}")
    @GET
    @Produces("application/json")
    public Response getCustomer(@PathParam("id") String id) {
        Response.ResponseBuilder builder = Response.ok(id, "application/json");
        Date date = new Date(System.currentTimeMillis()+10000);
        builder.expires(date);
        return builder.build();
    }

    @Path("/{id}")
    @GET
    @Produces("application/json")
    public Response getCustomer(@PathParam("id") int id) {
        CacheControl cc = new CacheControl();
        cc.setMaxAge(30);
        cc.setPrivate(true);
//        cc.setNoStore(true);

        Response.ResponseBuilder builder = Response.ok(id, "application/json");
        builder.cacheControl(cc);
        return builder.build();
    }


    /**
     * GET /customer/123 HTTP/1.1
     If-None-Match: "3141271342554322343200"
     * @param id
     * @param request
     * @return
     */
    @Path("/customer/{id}")
    @GET
    @Produces("application/json")
    public Response getCustomer(@PathParam("id") int id,
                                @Context Request request) {
        EntityTag tag = new EntityTag(Integer.toString(this.cust.hashCode()));
        CacheControl cc = new CacheControl();
        cc.setMaxAge(30);
        Response.ResponseBuilder builder = request.evaluatePreconditions(tag);
        if (builder != null) {
            builder.cacheControl(cc);
            return builder.build();
        }
        // Preconditions not met!
        builder = Response.ok(this.cust, "application/json");
//        builder.cacheControl(cc);
        builder.tag(tag);
        return builder.build();
    }


    /**
     * PUT /customer/123 HTTP/1.1
     If-Match: "3141271342554322343200"
     If-Unmodified-Since: Tue, 15 May 2009 09:56 EST
     Content-Type: application/json
     * @param id
     * @param request
     * @return
     */

    @Path("/customer/{id}")
    @PUT
    @Consumes("application/json")
    public Response updateCustomer(@PathParam("id") int id,
                                   @Context Request request) {
        EntityTag tag = new EntityTag(Integer.toString(this.cust.hashCode()));
        Date timestamp = new Date(System.currentTimeMillis()+3000); // get the timestampe
        Response.ResponseBuilder builder = request.evaluatePreconditions(timestamp, tag);
        if (builder != null) {
            // Preconditions not met!
            return builder.build();
        }
        //... perform the update ...
        this.cust = this.cust + "1";
        builder = Response.ok(this.cust);
        return builder.build();
    }

}
