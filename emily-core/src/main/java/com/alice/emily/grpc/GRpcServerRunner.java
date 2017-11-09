package com.alice.emily.grpc;

import com.alice.emily.core.SpringBeans;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.ServerServiceDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Hosts embedded gRPC server.
 */
@Slf4j
public class GRpcServerRunner implements CommandLineRunner, DisposableBean {

    @Autowired
    private GRpcProperties gRpcProperties;

    private GRpcServerBuilderConfigurer configurer;

    private Server server;

    public GRpcServerRunner(GRpcServerBuilderConfigurer configurer) {
        this.configurer = configurer;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!gRpcProperties.isServerConfigured()) {
            return;
        }
        log.info("Starting gRPC Server ...");

        List<ServerInterceptor> globalInterceptors = getGlobalInterceptors();

        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(gRpcProperties.getServer().getPort());

        // find and register all GRpcService-enabled beans
        List<String> beanNames = SpringBeans.getBeanNames(BindableService.class, GRpcService.class);
        for (String name : beanNames) {
            BindableService srv = SpringBeans.getBean(name, BindableService.class);
            if (srv != null) {
                ServerServiceDefinition serviceDefinition = srv.bindService();
                GRpcService gRpcServiceAnn = SpringBeans.findAnnotationOnBean(name, GRpcService.class);
                serviceDefinition = bindInterceptors(serviceDefinition, gRpcServiceAnn, globalInterceptors);
                serverBuilder.addService(serviceDefinition);
                log.info("Registered service: {}", AopUtils.getTargetClass(srv).getName());
            }
        }

        server = configurer.configure(serverBuilder).build().start();
        log.info("gRPC Server started, listening on port {}.", gRpcProperties.getServer().getPort());
        if (!gRpcProperties.getServer().isDaemon()) {
            startDaemonAwaitThread();
        }
    }

    @Override
    public void destroy() throws Exception {
        log.info("Shutting down gRPC server ...");
        Optional.ofNullable(server).ifPresent(Server::shutdown);
        log.info("gRPC server stopped.");
    }

    private ServerServiceDefinition bindInterceptors(ServerServiceDefinition serviceDefinition,
                                                     GRpcService gRpcService, List<ServerInterceptor> globalInterceptors) {

        Set<ServerInterceptor> interceptors = new HashSet<>();
        if (gRpcService.applyGlobalInterceptors()) {
            interceptors.addAll(globalInterceptors);
        }

        for (Class<? extends ServerInterceptor> interceptorClass : gRpcService.interceptors()) {
            if (SpringBeans.getBeanNames(interceptorClass).length > 0) {
                interceptors.add(SpringBeans.getBean(interceptorClass));
            } else {
                try {
                    interceptors.add(BeanUtils.instantiateClass(interceptorClass));
                } catch (BeanInstantiationException e) {
                    throw new BeanCreationException("Failed to create interceptor instance.", e);
                }
            }
        }

        return ServerInterceptors.intercept(serviceDefinition, new ArrayList<>(interceptors));
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread() {
            @Override
            public void run() {
                try {
                    GRpcServerRunner.this.server.awaitTermination();
                } catch (InterruptedException e) {
                    log.error("gRPC server stopped.", e);
                }
            }

        };
        awaitThread.setDaemon(gRpcProperties.getServer().isDaemon());
        awaitThread.setName("grpc-server");
        awaitThread.start();
    }

    private List<ServerInterceptor> getGlobalInterceptors() {
        List<String> beanNames = SpringBeans.getBeanNames(ServerInterceptor.class, GRpcGlobalInterceptor.class);
        if (CollectionUtils.isEmpty(beanNames)) {
            return Collections.emptyList();
        }
        return beanNames.stream()
                .map(name -> SpringBeans.getBean(name, ServerInterceptor.class))
                .collect(Collectors.toList());
    }
}