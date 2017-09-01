package com.alice.emily.example.zk.scene;


import com.alice.emily.example.zk.ZkFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zk_chs on 16/10/24.
 */
@Component
public class DefaultZkSceneProvider implements ZkSceneProvider {

    private static Logger log = LogManager.getLogger();

    private String path = "/scene";

    @Async
    @Override
    public void onCreate(Map scene) {
        this.operation(scene, ZkFactory.client, "create");
    }

    @Async
    @Override
    public void onUpdate(Map scene) {
        this.operation(scene, ZkFactory.client, "update");
    }

    @Async
    @Override
    public void onDelete(Long id) {
        Map scene = new HashMap<>();
        scene.put("id", id);
        this.operation(scene, ZkFactory.client, "delete");
    }

    @Override
    public String onGet(Long id){
        String scene = null;
        try {
            byte[] bytes = ZkFactory.client.getData().forPath(path + "/" + id);
            scene = new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scene;
    }

    /**
     * createMode:节点类型
     PERSISTENT：持久化节点
     PERSISTENT_SEQUENTIAL：持久化有序节点
     EPHEMERAL：临时节点（连接断开自动删除）
     EPHEMERAL_SEQUENTIAL：临时有序节点（连接断开自动删除）
     * @param scene
     * @param client
     * @param type
     */
    private void operation(Map scene, CuratorFramework client, String type) {
        try {
            if (type.equals("create")){
                if(client.checkExists().forPath(path + "/" + scene.get("id")) == null){
                    client.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.PERSISTENT)
                            .forPath(path + "/" + scene.get("id"), scene.toString().getBytes());
                }else{
                    log.info(path + "/" + scene.get("id") + " is exit");
                    client.setData().forPath(path + "/" + scene.get("id"), scene.toString().getBytes());
                }
            } else if(type.equals("update")) {
                client.setData().forPath(path + "/" + scene.get("id"), scene.toString().getBytes());
            } else if(type.equals("delete")){
                client.delete().forPath(path + "/" + scene.get("id"));
            }
        } catch (Exception e) {
            log.error(type + e.getMessage());
        }
    }
}
