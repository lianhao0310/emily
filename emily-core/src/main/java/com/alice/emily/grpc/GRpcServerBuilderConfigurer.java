package com.alice.emily.grpc;

import io.grpc.ServerBuilder;

public class GRpcServerBuilderConfigurer {
    public ServerBuilder<?> configure(ServerBuilder<?> serverBuilder) {
        return serverBuilder;
    }
}