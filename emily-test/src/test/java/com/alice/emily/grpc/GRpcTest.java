package com.alice.emily.grpc;

import com.alice.emily.grpc.scene.SceneRpc;
import com.alice.emily.grpc.scene.SceneRpcServiceGrpc;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by liupin on 2016/12/8.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
        "emily.grpc.server.port=50001",
        "emily.grpc.clients.test.host=localhost", "emily.grpc.clients.test.port=50001" })
public class GRpcTest extends AbstractJUnit4SpringContextTests {

    @GRpcClient("test")
    private SceneRpcServiceGrpc.SceneRpcServiceBlockingStub stub1;

    @GRpcClient("test")
    private SceneRpcServiceGrpc.SceneRpcServiceBlockingStub stub2;

    @Test
    public void testClient() {
        Assert.assertNotNull(stub1);
        Assert.assertNotNull(stub2);
        Assert.assertEquals(stub1.getChannel(), stub2.getChannel());

        SceneRpc.SceneRpcResponse response = stub1.getSceneByAppKey(
                SceneRpc.SceneRpcRequestByAppKey.newBuilder().setAppKey("just for test").build());
        Assertions.assertThat(response).isNotNull()
                .hasFieldOrPropertyWithValue("sceneId", 12128L)
                .hasFieldOrPropertyWithValue("appKey", "just for test");

        long token = System.currentTimeMillis();
        response = stub2.getSceneByToken(
                SceneRpc.SceneRpcRequestByToken.newBuilder().setToken(token).build());
        Assertions.assertThat(response).isNotNull()
                .hasFieldOrPropertyWithValue("sceneId", 12128L)
                .hasFieldOrPropertyWithValue("token", token);
    }

}
