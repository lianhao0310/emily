package com.alice.emily.example.okhttp.resource.subresource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Created by Lianhao on 2017/8/16.
 */
public class ApiV1Resource extends ApiResource{
    @GET
    @Path("/{id}")
    @Override
    public String getId(@PathParam("id") int id){
        return "apiv1 id is " + id;
    }
}
