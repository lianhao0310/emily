package com.alice.emily.grpc.scene;

import com.alice.emily.grpc.GRpcService;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

/**
 * Created by lianhao on 2017/7/28.
 */
@Log4j2
@GRpcService
public class SceneRpcServiceImpl extends SceneRpcServiceGrpc.SceneRpcServiceImplBase {

    @Override
    public void getSceneByAppKey(SceneRpc.SceneRpcRequestByAppKey request, StreamObserver<SceneRpc.SceneRpcResponse> responseObserver) {
        log.info("getSceneByAppKey {}", request);
        responseObserver.onNext(prepareScene(request.getAppKey(), null, null));
        responseObserver.onCompleted();
    }

    @Override
    public void getSceneByToken(SceneRpc.SceneRpcRequestByToken request, StreamObserver<SceneRpc.SceneRpcResponse> responseObserver) {
        log.info("getSceneByToken {}", request);
        responseObserver.onNext(prepareScene(null, request.getToken(), null));
        responseObserver.onCompleted();
    }

    @Override
    public void getSceneBySceneId(SceneRpc.SceneRpcRequestBySceneId request, StreamObserver<SceneRpc.SceneRpcResponse> responseObserver) {
        log.info("getSceneBySceneId {}", request);
        responseObserver.onNext(prepareScene(null, null, request.getSceneId()));
        responseObserver.onCompleted();
    }

    private SceneRpc.SceneRpcResponse prepareScene(String appKey, Long token, Long sceneId) {
        return SceneRpc.SceneRpcResponse.newBuilder()
                .setSceneId(sceneId == null ? 12128 : sceneId)
                .setSceneName("1481686348017")
                .setAppKey(StringUtils.isEmpty(appKey) ? "8dd48e174e6240838d989b9eca80a993" : appKey)
                .setSecretKey("fa9b954af3d742f79fcf048017cc278c")
                .setAvailable(true)
                .setToken(token == null ? 1481686348017L : token)
                .setCreateAt(1481736748000L)
                .build();
    }
}
