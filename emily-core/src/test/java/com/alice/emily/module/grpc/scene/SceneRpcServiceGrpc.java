package com.alice.emily.module.grpc.scene;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.1)",
    comments = "Source: scene.proto")
public class SceneRpcServiceGrpc {

  private SceneRpcServiceGrpc() {}

  public static final String SERVICE_NAME = "SceneRpcService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<SceneRpc.SceneRpcRequestByAppKey,
      SceneRpc.SceneRpcResponse> METHOD_GET_SCENE_BY_APP_KEY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "SceneRpcService", "getSceneByAppKey"),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcRequestByAppKey.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<SceneRpc.SceneRpcRequestByToken,
      SceneRpc.SceneRpcResponse> METHOD_GET_SCENE_BY_TOKEN =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "SceneRpcService", "getSceneByToken"),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcRequestByToken.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<SceneRpc.SceneRpcRequestBySceneId,
      SceneRpc.SceneRpcResponse> METHOD_GET_SCENE_BY_SCENE_ID =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "SceneRpcService", "getSceneBySceneId"),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcRequestBySceneId.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<SceneRpc.SceneRpcRequestByAppName,
      SceneRpc.SceneRpcResponses> METHOD_GET_SCENES_BY_APP_NAME =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "SceneRpcService", "getScenesByAppName"),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcRequestByAppName.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcResponses.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<SceneRpc.SceneRpcRequestByApMac,
      SceneRpc.SceneRpcResponses> METHOD_GET_SCENES_BY_AP_MAC =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "SceneRpcService", "getScenesByApMac"),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcRequestByApMac.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcResponses.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<SceneRpc.SceneRpcRequestByEmail,
      SceneRpc.SceneRpcResponses> METHOD_GET_SCENES_BY_EMAIL =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "SceneRpcService", "getScenesByEmail"),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcRequestByEmail.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcResponses.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<SceneRpc.SceneRpcRequestByMapId,
      SceneRpc.SceneRpcResponses> METHOD_GET_SCENES_BY_MAP_ID =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "SceneRpcService", "getScenesByMapId"),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcRequestByMapId.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(SceneRpc.SceneRpcResponses.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SceneRpcServiceStub newStub(io.grpc.Channel channel) {
    return new SceneRpcServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SceneRpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new SceneRpcServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static SceneRpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new SceneRpcServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class SceneRpcServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getSceneByAppKey(SceneRpc.SceneRpcRequestByAppKey request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENE_BY_APP_KEY, responseObserver);
    }

    /**
     */
    public void getSceneByToken(SceneRpc.SceneRpcRequestByToken request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENE_BY_TOKEN, responseObserver);
    }

    /**
     */
    public void getSceneBySceneId(SceneRpc.SceneRpcRequestBySceneId request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENE_BY_SCENE_ID, responseObserver);
    }

    /**
     */
    public void getScenesByAppName(SceneRpc.SceneRpcRequestByAppName request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENES_BY_APP_NAME, responseObserver);
    }

    /**
     */
    public void getScenesByApMac(SceneRpc.SceneRpcRequestByApMac request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENES_BY_AP_MAC, responseObserver);
    }

    /**
     */
    public void getScenesByEmail(SceneRpc.SceneRpcRequestByEmail request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENES_BY_EMAIL, responseObserver);
    }

    /**
     */
    public void getScenesByMapId(SceneRpc.SceneRpcRequestByMapId request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENES_BY_MAP_ID, responseObserver);
    }

    @Override
    public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_GET_SCENE_BY_APP_KEY,
            asyncUnaryCall(
              new MethodHandlers<
                SceneRpc.SceneRpcRequestByAppKey,
                SceneRpc.SceneRpcResponse>(
                  this, METHODID_GET_SCENE_BY_APP_KEY)))
          .addMethod(
            METHOD_GET_SCENE_BY_TOKEN,
            asyncUnaryCall(
              new MethodHandlers<
                SceneRpc.SceneRpcRequestByToken,
                SceneRpc.SceneRpcResponse>(
                  this, METHODID_GET_SCENE_BY_TOKEN)))
          .addMethod(
            METHOD_GET_SCENE_BY_SCENE_ID,
            asyncUnaryCall(
              new MethodHandlers<
                SceneRpc.SceneRpcRequestBySceneId,
                SceneRpc.SceneRpcResponse>(
                  this, METHODID_GET_SCENE_BY_SCENE_ID)))
          .addMethod(
            METHOD_GET_SCENES_BY_APP_NAME,
            asyncUnaryCall(
              new MethodHandlers<
                SceneRpc.SceneRpcRequestByAppName,
                SceneRpc.SceneRpcResponses>(
                  this, METHODID_GET_SCENES_BY_APP_NAME)))
          .addMethod(
            METHOD_GET_SCENES_BY_AP_MAC,
            asyncUnaryCall(
              new MethodHandlers<
                SceneRpc.SceneRpcRequestByApMac,
                SceneRpc.SceneRpcResponses>(
                  this, METHODID_GET_SCENES_BY_AP_MAC)))
          .addMethod(
            METHOD_GET_SCENES_BY_EMAIL,
            asyncUnaryCall(
              new MethodHandlers<
                SceneRpc.SceneRpcRequestByEmail,
                SceneRpc.SceneRpcResponses>(
                  this, METHODID_GET_SCENES_BY_EMAIL)))
          .addMethod(
            METHOD_GET_SCENES_BY_MAP_ID,
            asyncUnaryCall(
              new MethodHandlers<
                SceneRpc.SceneRpcRequestByMapId,
                SceneRpc.SceneRpcResponses>(
                  this, METHODID_GET_SCENES_BY_MAP_ID)))
          .build();
    }
  }

  /**
   */
  public static final class SceneRpcServiceStub extends io.grpc.stub.AbstractStub<SceneRpcServiceStub> {
    private SceneRpcServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SceneRpcServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected SceneRpcServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SceneRpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void getSceneByAppKey(SceneRpc.SceneRpcRequestByAppKey request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_APP_KEY, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSceneByToken(SceneRpc.SceneRpcRequestByToken request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_TOKEN, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSceneBySceneId(SceneRpc.SceneRpcRequestBySceneId request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_SCENE_ID, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getScenesByAppName(SceneRpc.SceneRpcRequestByAppName request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_APP_NAME, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getScenesByApMac(SceneRpc.SceneRpcRequestByApMac request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_AP_MAC, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getScenesByEmail(SceneRpc.SceneRpcRequestByEmail request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_EMAIL, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getScenesByMapId(SceneRpc.SceneRpcRequestByMapId request,
        io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_MAP_ID, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SceneRpcServiceBlockingStub extends io.grpc.stub.AbstractStub<SceneRpcServiceBlockingStub> {
    private SceneRpcServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SceneRpcServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected SceneRpcServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SceneRpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public SceneRpc.SceneRpcResponse getSceneByAppKey(SceneRpc.SceneRpcRequestByAppKey request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENE_BY_APP_KEY, getCallOptions(), request);
    }

    /**
     */
    public SceneRpc.SceneRpcResponse getSceneByToken(SceneRpc.SceneRpcRequestByToken request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENE_BY_TOKEN, getCallOptions(), request);
    }

    /**
     */
    public SceneRpc.SceneRpcResponse getSceneBySceneId(SceneRpc.SceneRpcRequestBySceneId request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENE_BY_SCENE_ID, getCallOptions(), request);
    }

    /**
     */
    public SceneRpc.SceneRpcResponses getScenesByAppName(SceneRpc.SceneRpcRequestByAppName request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENES_BY_APP_NAME, getCallOptions(), request);
    }

    /**
     */
    public SceneRpc.SceneRpcResponses getScenesByApMac(SceneRpc.SceneRpcRequestByApMac request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENES_BY_AP_MAC, getCallOptions(), request);
    }

    /**
     */
    public SceneRpc.SceneRpcResponses getScenesByEmail(SceneRpc.SceneRpcRequestByEmail request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENES_BY_EMAIL, getCallOptions(), request);
    }

    /**
     */
    public SceneRpc.SceneRpcResponses getScenesByMapId(SceneRpc.SceneRpcRequestByMapId request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENES_BY_MAP_ID, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SceneRpcServiceFutureStub extends io.grpc.stub.AbstractStub<SceneRpcServiceFutureStub> {
    private SceneRpcServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SceneRpcServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected SceneRpcServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SceneRpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<SceneRpc.SceneRpcResponse> getSceneByAppKey(
        SceneRpc.SceneRpcRequestByAppKey request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_APP_KEY, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<SceneRpc.SceneRpcResponse> getSceneByToken(
        SceneRpc.SceneRpcRequestByToken request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_TOKEN, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<SceneRpc.SceneRpcResponse> getSceneBySceneId(
        SceneRpc.SceneRpcRequestBySceneId request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_SCENE_ID, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<SceneRpc.SceneRpcResponses> getScenesByAppName(
        SceneRpc.SceneRpcRequestByAppName request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_APP_NAME, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<SceneRpc.SceneRpcResponses> getScenesByApMac(
        SceneRpc.SceneRpcRequestByApMac request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_AP_MAC, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<SceneRpc.SceneRpcResponses> getScenesByEmail(
        SceneRpc.SceneRpcRequestByEmail request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_EMAIL, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<SceneRpc.SceneRpcResponses> getScenesByMapId(
        SceneRpc.SceneRpcRequestByMapId request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_MAP_ID, getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_SCENE_BY_APP_KEY = 0;
  private static final int METHODID_GET_SCENE_BY_TOKEN = 1;
  private static final int METHODID_GET_SCENE_BY_SCENE_ID = 2;
  private static final int METHODID_GET_SCENES_BY_APP_NAME = 3;
  private static final int METHODID_GET_SCENES_BY_AP_MAC = 4;
  private static final int METHODID_GET_SCENES_BY_EMAIL = 5;
  private static final int METHODID_GET_SCENES_BY_MAP_ID = 6;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SceneRpcServiceImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(SceneRpcServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_SCENE_BY_APP_KEY:
          serviceImpl.getSceneByAppKey((SceneRpc.SceneRpcRequestByAppKey) request,
              (io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponse>) responseObserver);
          break;
        case METHODID_GET_SCENE_BY_TOKEN:
          serviceImpl.getSceneByToken((SceneRpc.SceneRpcRequestByToken) request,
              (io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponse>) responseObserver);
          break;
        case METHODID_GET_SCENE_BY_SCENE_ID:
          serviceImpl.getSceneBySceneId((SceneRpc.SceneRpcRequestBySceneId) request,
              (io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponse>) responseObserver);
          break;
        case METHODID_GET_SCENES_BY_APP_NAME:
          serviceImpl.getScenesByAppName((SceneRpc.SceneRpcRequestByAppName) request,
              (io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses>) responseObserver);
          break;
        case METHODID_GET_SCENES_BY_AP_MAC:
          serviceImpl.getScenesByApMac((SceneRpc.SceneRpcRequestByApMac) request,
              (io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses>) responseObserver);
          break;
        case METHODID_GET_SCENES_BY_EMAIL:
          serviceImpl.getScenesByEmail((SceneRpc.SceneRpcRequestByEmail) request,
              (io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses>) responseObserver);
          break;
        case METHODID_GET_SCENES_BY_MAP_ID:
          serviceImpl.getScenesByMapId((SceneRpc.SceneRpcRequestByMapId) request,
              (io.grpc.stub.StreamObserver<SceneRpc.SceneRpcResponses>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_GET_SCENE_BY_APP_KEY,
        METHOD_GET_SCENE_BY_TOKEN,
        METHOD_GET_SCENE_BY_SCENE_ID,
        METHOD_GET_SCENES_BY_APP_NAME,
        METHOD_GET_SCENES_BY_AP_MAC,
        METHOD_GET_SCENES_BY_EMAIL,
        METHOD_GET_SCENES_BY_MAP_ID);
  }

}
