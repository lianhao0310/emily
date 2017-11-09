package com.alice.emily.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.zookeeper.data.ACL;
import org.springframework.core.convert.ConversionService;

import java.util.List;

/**
 * Created by lianhao on 2017/6/13.
 */
public interface CuratorOperations {

    CuratorFramework client();

    ConversionService conversion();

    boolean nodeExists(String path);

    String createNode(String path);

    String createNode(String path, Object data);

    void deleteNode(String path);

    List<String> getNodeChildren(String path);

    <T> T getNodeData(String path, Class<T> type);

    void setNodeData(String path, Object data);

    void setNodeData(String path, Object data, Integer version);

    List<ACL> getNodeACL(String path);

    void setNodeACL(String path, List<ACL> aclList);

    void setNodeACL(String path, List<ACL> aclList, Integer version);

    NodeCache createNodeCache(String path);

    PathChildrenCache createPathChildrenCache(String path);

    TreeCache createTreeCache(String path);
}
