package com.alice.emily.curator;

import com.alice.emily.curator.cache.CuratorCacheListener;
import com.alice.emily.curator.cache.CuratorCacheType;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by lianhao on 2017/6/14.
 */
public class CacheTestListener {

    public static final String SCENE_PATH = "/data/scene";
    public static final String MAP_PATH = "/data/map";
    public static final String POI_PATH = "/data/poi";

    @CuratorCacheListener("${curator.test.path.node-cache}")
    public void onSceneNode(@Payload String data,
                            @Header("path") String path,
                            ChildData childData) {
        System.out.format("****** [%s]: %s", path, data);
        System.out.println();

        assertThat(childData)
                .isNotNull()
                .hasFieldOrPropertyWithValue("path", path)
                .hasFieldOrPropertyWithValue("path", "/data/scene")
                .hasFieldOrPropertyWithValue("data", data == null ? new byte[0] : data.getBytes());
    }

    @CuratorCacheListener(value = MAP_PATH, type = CuratorCacheType.PATH_CHILDREN)
    void onMapPathChildren(@Payload(required = false) String data,
                                  @Header("path") String path,
                                  @Header("type") String type,
                                  PathChildrenCacheEvent event,
                                  CuratorFramework client) {
        System.out.format("****** [%s]: %s %s", path, type, data);
        System.out.println();

        assertThat(event).isNotNull();
        assertThat(client).isNotNull();
        assertThat(type).isNotBlank().isEqualTo(event.getType().name());
        assertThat(path).startsWith("/data/map");

        assertThat(event.getData())
                .isNotNull()
                .hasFieldOrPropertyWithValue("path", path)
                .hasFieldOrPropertyWithValue("data", data == null ? new byte[0] : data.getBytes());
    }

    @CuratorCacheListener(value = POI_PATH, type = CuratorCacheType.TREE)
    public void onPoiTree(@Payload(required = false) String data,
                          @Header("path") String path,
                          @Header("type") String type,
                          TreeCacheEvent event,
                          CuratorOperations operations) {
        System.out.format("****** [%s]: %s %s", path, type, data);
        System.out.println();

        assertThat(event).isNotNull();
        assertThat(operations).isNotNull();
        assertThat(type).isNotBlank().isEqualTo(event.getType().name());
        assertThat(path).startsWith("/data/poi");

        assertThat(event.getData())
                .isNotNull()
                .hasFieldOrPropertyWithValue("path", path);
    }
}
