package com.alice.emily.curator;

import com.alice.emily.zookeeper.ZooKeeperUtils;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.SetACLBuilder;
import org.apache.curator.framework.api.SetDataBuilder;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.ACL;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

/**
 * Created by lianhao on 2017/6/13.
 */
public class CuratorTemplate implements CuratorOperations {

    protected final CuratorFramework client;
    protected final ThreadPoolTaskExecutor executor;
    protected final ConversionService conversion;

    public CuratorTemplate(@NonNull CuratorFramework client,
                           ThreadPoolTaskExecutor executor,
                           @NonNull ConversionService conversion) {
        this.client = client;
        this.executor = executor;
        this.conversion = conversion;
    }

    @Override
    public CuratorFramework client() {
        return client;
    }

    @Override
    public ConversionService conversion() {
        return conversion;
    }

    @SneakyThrows
    @Override
    public boolean nodeExists(@NonNull String path) {
        return client.checkExists().forPath(path) != null;
    }

    @SneakyThrows
    @Override
    public String createNode(@NonNull String path) {
        return client.create().creatingParentContainersIfNeeded().forPath(path);
    }

    @SneakyThrows
    @Override
    public String createNode(@NonNull String path, Object data) {
        if (data == null) {
            return createNode(path);
        }
        byte[] bytes = conversion.convert(data, byte[].class);
        return client.create().creatingParentContainersIfNeeded().forPath(path, bytes);
    }

    @SneakyThrows
    @Override
    public void deleteNode(@NonNull String path) {
        client.delete().deletingChildrenIfNeeded().forPath(path);
    }

    @SneakyThrows
    @Override
    public List<String> getNodeChildren(@NonNull String path) {
        return client.getChildren().forPath(path);
    }

    @SneakyThrows
    @Override
    public <T> T getNodeData(@NonNull String path, Class<T> type) {
        byte[] bytes = client.getData().forPath(path);
        return conversion.convert(bytes, type);
    }

    @Override
    public void setNodeData(@NonNull String path, Object data) {
        setNodeData(path, data, null);
    }

    @Override
    public void setNodeData(@NonNull String path, Object data, Integer version) {
        try {
            doSetNodeData(path, data, version);
        } catch (KeeperException.NoNodeException e) {
            createNode(path, data);
        } catch (Exception e) {
            throw ZooKeeperUtils.wrapThrowable(e, "Cannot set node data for " + path);
        }
    }

    private void doSetNodeData(@NonNull String path, Object data, Integer version) throws Exception {
        SetDataBuilder setDataBuilder = client.setData();

        if (version != null) {
            setDataBuilder.withVersion(version);
        }

        if (data != null) {
            byte[] bytes = conversion.convert(data, byte[].class);
            setDataBuilder.forPath(path, bytes);
        } else {
            setDataBuilder.forPath(path);
        }
    }

    @SneakyThrows
    @Override
    public List<ACL> getNodeACL(@NonNull String path) {
        return client.getACL().forPath(path);
    }

    @Override
    public void setNodeACL(@NonNull String path, @NonNull List<ACL> aclList) {
        setNodeACL(path, aclList, null);
    }

    @SneakyThrows
    @Override
    public void setNodeACL(@NonNull String path, @NonNull List<ACL> aclList, Integer version) {
        SetACLBuilder setACLBuilder = client.setACL();
        if (version != null) {
            setACLBuilder.withVersion(version);
        }
        setACLBuilder.withACL(aclList).forPath(path);
    }

    @Override
    public NodeCache createNodeCache(String path) {
        return new NodeCache(client, path);
    }

    @Override
    public PathChildrenCache createPathChildrenCache(String path) {
        return new PathChildrenCache(client, path, true, false, executor);
    }

    @Override
    public TreeCache createTreeCache(String path) {
        return TreeCache.newBuilder(client, path)
                .setCacheData(false)
                .setCreateParentNodes(true)
                .setExecutor(executor)
                .build();
    }
}
