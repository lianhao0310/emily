package com.alice.emily.module.grpc;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.alice.emily.utils.Errors;
import com.alice.emily.utils.LOG;
import com.alice.emily.utils.Reflection;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractStub;
import org.springframework.beans.factory.DisposableBean;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lianhao on 2017/2/6.
 */
public class GRpcClientFactory implements DisposableBean {

    private Map<String, ManagedChannel> channelMap;
    private Map<Class<AbstractStub>, Method> factoryMethods;
    private final GRpcProperties gRpcProperties;

    public GRpcClientFactory(GRpcProperties gRpcProperties) {
        this.gRpcProperties = gRpcProperties;
        this.factoryMethods = Maps.newConcurrentMap();
        this.channelMap = new HashMap<>();
    }

    public AbstractStub<?> buildStub(Class<?> stubClass, String key) {
        try {
            GRpcProperties.ClientConfig config = gRpcProperties.getClients().get(key);
            ManagedChannel channel = channelMap.computeIfAbsent(key, k -> getManagedChannel(config));
            Class<AbstractStub> rawType = Reflection.getRawType(stubClass);
            LOG.RPC.debug("Build gRPC client for {}: {}", key, rawType.getSimpleName());
            Method method = factoryMethods.computeIfAbsent(rawType, this::getFactoryMethod);
            Object stub = method.invoke(null, channel);
            return Reflection.cast(stub);
        } catch (Exception e) {
            throw Errors.service(e, "cannot build stub for %s", stubClass.getName());
        }
    }

    private ManagedChannel getManagedChannel(GRpcProperties.ClientConfig config) {
        return ManagedChannelBuilder.forAddress(config.getHost(), config.getPort())
                .usePlaintext(true)
                .build();
    }

    private Method getFactoryMethod(Class<AbstractStub> stubClass) {
        Class<?> rpcClass = stubClass.getDeclaringClass();
        Preconditions.checkNotNull(rpcClass, "cannot found rpc class for %s", stubClass.getName());
        for (Method method : rpcClass.getMethods()) {
            if (method.getReturnType().equals(stubClass)) {
                Parameter[] parameters = method.getParameters();
                if (parameters.length == 1 && parameters[0].getType().isAssignableFrom(Channel.class)) {
                    method.setAccessible(true);
                    return method;
                }
            }
        }
        throw Errors.service("cannot found factory method for %s in rpc class %s", stubClass.getName(), rpcClass.getName());
    }

    @Override
    public void destroy() throws Exception {
        if (channelMap == null || channelMap.isEmpty()) return;
        channelMap.values().forEach(ManagedChannel::shutdown);
        channelMap = null;
    }
}
