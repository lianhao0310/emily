package com.alice.emily.module.grpc;

import io.grpc.ServerBuilder;

public class GRpcServerBuilderConfigurer {
    public ServerBuilder<?> configure(ServerBuilder<?> serverBuilder) {
        return serverBuilder;
    }
}