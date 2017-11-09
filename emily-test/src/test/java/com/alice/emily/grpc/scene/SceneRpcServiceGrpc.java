package com.alice.emily.grpc.scene;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.5.0)",
    comments = "Source: scene.proto")
public final class SceneRpcServiceGrpc {

  private SceneRpcServiceGrpc() {}

  public static final String SERVICE_NAME = "SceneRpcService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppKey,
      com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> METHOD_GET_SCENE_BY_APP_KEY =
      io.grpc.MethodDescriptor.<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppKey, com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "SceneRpcService", "getSceneByAppKey"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppKey.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByToken,
      com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> METHOD_GET_SCENE_BY_TOKEN =
      io.grpc.MethodDescriptor.<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByToken, com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "SceneRpcService", "getSceneByToken"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByToken.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestBySceneId,
      com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> METHOD_GET_SCENE_BY_SCENE_ID =
      io.grpc.MethodDescriptor.<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestBySceneId, com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "SceneRpcService", "getSceneBySceneId"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestBySceneId.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppName,
      com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> METHOD_GET_SCENES_BY_APP_NAME =
      io.grpc.MethodDescriptor.<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppName, com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "SceneRpcService", "getScenesByAppName"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppName.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByApMac,
      com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> METHOD_GET_SCENES_BY_AP_MAC =
      io.grpc.MethodDescriptor.<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByApMac, com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "SceneRpcService", "getScenesByApMac"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByApMac.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByEmail,
      com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> METHOD_GET_SCENES_BY_EMAIL =
      io.grpc.MethodDescriptor.<com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByEmail, com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "SceneRpcService", "getScenesByEmail"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByEmail.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses.getDefaultInstance()))
          .build();

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
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
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
    public void getSceneByAppKey(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppKey request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENE_BY_APP_KEY, responseObserver);
    }

    /**
     */
    public void getSceneByToken(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByToken request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENE_BY_TOKEN, responseObserver);
    }

    /**
     */
    public void getSceneBySceneId(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestBySceneId request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENE_BY_SCENE_ID, responseObserver);
    }

    /**
     */
    public void getScenesByAppName(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppName request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENES_BY_APP_NAME, responseObserver);
    }

    /**
     */
    public void getScenesByApMac(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByApMac request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENES_BY_AP_MAC, responseObserver);
    }

    /**
     */
    public void getScenesByEmail(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByEmail request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SCENES_BY_EMAIL, responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_GET_SCENE_BY_APP_KEY,
            asyncUnaryCall(
              new MethodHandlers<
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppKey,
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse>(
                  this, METHODID_GET_SCENE_BY_APP_KEY)))
          .addMethod(
            METHOD_GET_SCENE_BY_TOKEN,
            asyncUnaryCall(
              new MethodHandlers<
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByToken,
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse>(
                  this, METHODID_GET_SCENE_BY_TOKEN)))
          .addMethod(
            METHOD_GET_SCENE_BY_SCENE_ID,
            asyncUnaryCall(
              new MethodHandlers<
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestBySceneId,
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse>(
                  this, METHODID_GET_SCENE_BY_SCENE_ID)))
          .addMethod(
            METHOD_GET_SCENES_BY_APP_NAME,
            asyncUnaryCall(
              new MethodHandlers<
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppName,
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses>(
                  this, METHODID_GET_SCENES_BY_APP_NAME)))
          .addMethod(
            METHOD_GET_SCENES_BY_AP_MAC,
            asyncUnaryCall(
              new MethodHandlers<
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByApMac,
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses>(
                  this, METHODID_GET_SCENES_BY_AP_MAC)))
          .addMethod(
            METHOD_GET_SCENES_BY_EMAIL,
            asyncUnaryCall(
              new MethodHandlers<
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByEmail,
                com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses>(
                  this, METHODID_GET_SCENES_BY_EMAIL)))
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
    public void getSceneByAppKey(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppKey request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_APP_KEY, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSceneByToken(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByToken request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_TOKEN, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSceneBySceneId(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestBySceneId request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_SCENE_ID, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getScenesByAppName(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppName request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_APP_NAME, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getScenesByApMac(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByApMac request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_AP_MAC, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getScenesByEmail(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByEmail request,
        io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_EMAIL, getCallOptions()), request, responseObserver);
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
    public com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse getSceneByAppKey(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppKey request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENE_BY_APP_KEY, getCallOptions(), request);
    }

    /**
     */
    public com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse getSceneByToken(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByToken request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENE_BY_TOKEN, getCallOptions(), request);
    }

    /**
     */
    public com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse getSceneBySceneId(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestBySceneId request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENE_BY_SCENE_ID, getCallOptions(), request);
    }

    /**
     */
    public com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses getScenesByAppName(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppName request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENES_BY_APP_NAME, getCallOptions(), request);
    }

    /**
     */
    public com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses getScenesByApMac(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByApMac request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENES_BY_AP_MAC, getCallOptions(), request);
    }

    /**
     */
    public com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses getScenesByEmail(com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByEmail request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SCENES_BY_EMAIL, getCallOptions(), request);
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
    public com.google.common.util.concurrent.ListenableFuture<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> getSceneByAppKey(
        com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppKey request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_APP_KEY, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> getSceneByToken(
        com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByToken request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_TOKEN, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse> getSceneBySceneId(
        com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestBySceneId request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENE_BY_SCENE_ID, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> getScenesByAppName(
        com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppName request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_APP_NAME, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> getScenesByApMac(
        com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByApMac request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_AP_MAC, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses> getScenesByEmail(
        com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByEmail request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SCENES_BY_EMAIL, getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_SCENE_BY_APP_KEY = 0;
  private static final int METHODID_GET_SCENE_BY_TOKEN = 1;
  private static final int METHODID_GET_SCENE_BY_SCENE_ID = 2;
  private static final int METHODID_GET_SCENES_BY_APP_NAME = 3;
  private static final int METHODID_GET_SCENES_BY_AP_MAC = 4;
  private static final int METHODID_GET_SCENES_BY_EMAIL = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SceneRpcServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SceneRpcServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_SCENE_BY_APP_KEY:
          serviceImpl.getSceneByAppKey((com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppKey) request,
              (io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse>) responseObserver);
          break;
        case METHODID_GET_SCENE_BY_TOKEN:
          serviceImpl.getSceneByToken((com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByToken) request,
              (io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse>) responseObserver);
          break;
        case METHODID_GET_SCENE_BY_SCENE_ID:
          serviceImpl.getSceneBySceneId((com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestBySceneId) request,
              (io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponse>) responseObserver);
          break;
        case METHODID_GET_SCENES_BY_APP_NAME:
          serviceImpl.getScenesByAppName((com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByAppName) request,
              (io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses>) responseObserver);
          break;
        case METHODID_GET_SCENES_BY_AP_MAC:
          serviceImpl.getScenesByApMac((com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByApMac) request,
              (io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses>) responseObserver);
          break;
        case METHODID_GET_SCENES_BY_EMAIL:
          serviceImpl.getScenesByEmail((com.alice.emily.grpc.scene.SceneRpc.SceneRpcRequestByEmail) request,
              (io.grpc.stub.StreamObserver<com.alice.emily.grpc.scene.SceneRpc.SceneRpcResponses>) responseObserver);
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

  private static final class SceneRpcServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.alice.emily.grpc.scene.SceneRpc.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SceneRpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SceneRpcServiceDescriptorSupplier())
              .addMethod(METHOD_GET_SCENE_BY_APP_KEY)
              .addMethod(METHOD_GET_SCENE_BY_TOKEN)
              .addMethod(METHOD_GET_SCENE_BY_SCENE_ID)
              .addMethod(METHOD_GET_SCENES_BY_APP_NAME)
              .addMethod(METHOD_GET_SCENES_BY_AP_MAC)
              .addMethod(METHOD_GET_SCENES_BY_EMAIL)
              .build();
        }
      }
    }
    return result;
  }
}
