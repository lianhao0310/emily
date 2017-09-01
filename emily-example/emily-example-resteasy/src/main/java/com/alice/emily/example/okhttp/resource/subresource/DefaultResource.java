package com.alice.emily.example.okhttp.resource.subresource;

import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Created by Lianhao on 2017/8/16.
 */
@Path("/default")
@Component
public class DefaultResource {
    @Path("/api/{version}")
    public ApiResource getVersion(@PathParam("version") String version){
        ApiResource resource = locateApiResource(version);
        return resource;
    }

    protected ApiResource locateApiResource(String version){
        if(version.equals("v1")){
            return new ApiV1Resource();
        }else if(version.equals("v2")){
            return new ApiV2Resource();
        }
        return new ApiResource();
    }
}
