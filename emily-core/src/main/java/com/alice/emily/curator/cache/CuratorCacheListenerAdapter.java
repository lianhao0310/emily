package com.alice.emily.curator.cache;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.MoreExecutors;
import com.alice.emily.core.SpringContext;
import com.alice.emily.curator.CuratorOperations;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.utils.CloseableUtils;
import org.springframework.context.Lifecycle;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Closeable;
import java.util.Set;
import java.util.concurrent.Executor;

@Log4j2
public class CuratorCacheListenerAdapter implements Lifecycle {

    private final String path;
    private final CuratorCacheType type;

    private CuratorOperations operations;
    private Executor executor;

    private Closeable closeable;
    private Set<InvocableHandlerMethod> handlerMethods;

    public CuratorCacheListenerAdapter(@NonNull String path,
                                       @NonNull CuratorCacheType type) {
        this.path = SpringContext.resolvePlaceHolder(path);
        this.type = type;
    }

    public void addHandlerMethod(InvocableHandlerMethod handlerMethod) {
        if (CollectionUtils.isEmpty(handlerMethods)) {
            handlerMethods = Sets.newHashSet();
        }
        handlerMethods.add(handlerMethod);
    }

    public void setOperations(CuratorOperations operations) {
        this.operations = operations;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void start() {
        if (isRunning() || CollectionUtils.isEmpty(handlerMethods)) {
            return;
        }

        Assert.notNull(operations, "CuratorOperations must be provided");
        if (executor == null) {
            executor = MoreExecutors.directExecutor();
        }

        switch (type) {
            case NODE:
                try {
                    NodeCache nodeCache = operations.createNodeCache(path);
                    nodeCache.start(true);
                    nodeCache.getListenable().addListener(() ->
                            invokeHandlerMethods(operations.client(), nodeCache.getCurrentData()), executor);
                    this.closeable = nodeCache;
                } catch (Exception e) {
                    log.error("Start NodeCache for {} failed", path, e);
                }
                break;
            case PATH_CHILDREN:
                try {
                    PathChildrenCache pathChildrenCache = operations.createPathChildrenCache(path);
                    pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
                    pathChildrenCache.getListenable().addListener(this::invokeHandlerMethods, executor);
                    this.closeable = pathChildrenCache;
                } catch (Exception e) {
                    log.error("Start PathChildrenCache for {} failed", path, e);
                }
                break;
            case TREE:
                try {
                    TreeCache treeCache = operations.createTreeCache(path);
                    treeCache.start();
                    treeCache.getListenable().addListener(this::invokeHandlerMethods, executor);
                    this.closeable = treeCache;
                } catch (Exception e) {
                    log.error("Start TreeCache for {} failed", path, e);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void stop() {
        if (closeable != null) {
            CloseableUtils.closeQuietly(closeable);
            closeable = null;
        }
    }

    @Override
    public boolean isRunning() {
        return closeable != null;
    }

    private void invokeHandlerMethods(CuratorFramework client, Object event) {
        ChildData childData = null;
        Object type = null;

        if (event instanceof ChildData) {
            childData = (ChildData) event;
        } else if (event instanceof PathChildrenCacheEvent) {
            childData = ((PathChildrenCacheEvent) event).getData();
            type = ((PathChildrenCacheEvent) event).getType();
        } else if (event instanceof TreeCacheEvent) {
            childData = ((TreeCacheEvent) event).getData();
            type = ((TreeCacheEvent) event).getType();
        }

        if (childData == null) {
            // no data exist on event, just ignore for now
            return;
        }

        byte[] payload = childData.getData() == null ? new byte[0] : childData.getData();

        Message<byte[]> message = MessageBuilder
                .withPayload(payload)
                .setHeader("path", childData.getPath())
                .setHeader("stat", childData.getStat())
                .setHeader("type", type)
                .build();

        for (InvocableHandlerMethod handlerMethod : handlerMethods) {
            try {
                handlerMethod.invoke(message, event, client, closeable, operations);
            } catch (Exception e) {
                log.error("Invoke handler method {} with message {} failed", handlerMethod, message, e);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CuratorCacheListenerAdapter that = (CuratorCacheListenerAdapter) o;

        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}