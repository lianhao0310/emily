package com.alice.emily.example.resource;


import com.alice.emily.example.zk.scene.ZkSceneProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lianhao on 2017/8/18.
 */
@Path("/scene")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class SceneResource {
    @Autowired
    private ZkSceneProvider zkSceneProvider;

    @GET
    @Path("/get")
    public String get(@QueryParam("id") Long id){
        return zkSceneProvider.onGet(id);
    }

    @PUT
    @Path("/create")
    public void create(@QueryParam("id") Long id, @QueryParam("value") Long value){
        Map scene = new HashMap();
        scene.put("id", id);
        scene.put("value", value);
        zkSceneProvider.onCreate(scene);
    }

    @PUT
    @Path("/update")
    public void update(@QueryParam("id") Long id, @QueryParam("value") Long value){
        Map scene = new HashMap();
        scene.put("id", id);
        scene.put("value", value);
        zkSceneProvider.onUpdate(scene);
    }

    @PUT
    @Path("/delete")
    public void delete(@QueryParam("id") Long id){
        zkSceneProvider.onDelete(id);
    }
}
