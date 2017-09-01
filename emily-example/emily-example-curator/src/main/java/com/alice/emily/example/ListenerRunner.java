package com.alice.emily.example;


import com.alice.emily.example.zk.ZkFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Lianhao on 2017/8/21.
 */
//@Component
public class ListenerRunner implements CommandLineRunner {
    private String path = "/scene";

    @Autowired
    private ZkFactory zkFactory;

    @Override
    public void run(String... strings) throws Exception {
        ZkFactory.client = zkFactory.getInstance();
        setListenterThreeThree(ZkFactory.client, path);
    }

    // 监控 指定节点和节点下的所有的节点的变化--无限监听  可以进行本节点的删除(不在创建)
    public void setListenterThreeThree(CuratorFramework client, String path) throws Exception{
        ExecutorService pool = Executors.newCachedThreadPool();
        //设置节点的cache
        TreeCache treeCache = new TreeCache(client, path);
        //设置监听器和处理过程
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData data = event.getData();
                if(data !=null){
                    String scene = new String(data.getData());
                    switch (event.getType()) {
                        case NODE_ADDED:
                            System.out.println("NODE_ADDED : "+ data.getPath() +"  数据:"+ scene.toString());
                            break;
                        case NODE_REMOVED:
                            System.out.println("NODE_REMOVED : "+ data.getPath() +"  数据:"+ scene.toString());
                            break;
                        case NODE_UPDATED:
                            System.out.println("NODE_UPDATED : "+ data.getPath() +"  数据:"+ scene.toString());
                            break;

                        default:
                            break;
                    }
                }else{
                    System.out.println(event.getType());
                }
            }
        });
        //开始监听
        treeCache.start();

    }
}
