package com.alice.emily.module.grpc;

import io.grpc.ServerBuilder;

import java.io.IOException;

public class GRpcServerBuilderConfigurer {
    public ServerBuilder<?> configure(ServerBuilder<?> serverBuilder) throws IOException {
        return serverBuilder;
    }
}