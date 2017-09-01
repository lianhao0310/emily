package com.palmaplus.euphoria.module.grpc;

import com.palmaplus.euphoria.autoconfigure.GRpcAutoConfiguration;
import com.palmaplus.euphoria.module.grpc.scene.SceneRpcServiceGrpc;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by liupin on 2016/12/8.
 */
@SpringBootTest(classes = GRpcAutoConfiguration.class)
public class GRpcTest extends AbstractJUnit4SpringContextTests {

    @GRpcClient("pop")
    private SceneRpcServiceGrpc.SceneRpcServiceBlockingStub stub1;

    @GRpcClient("pop")
    private SceneRpcServiceGrpc.SceneRpcServiceBlockingStub stub2;

    @Test
    public void testClient() {
        Assert.assertNotNull(stub1);
        Assert.assertNotNull(stub2);
        Assert.assertEquals(stub1.getChannel(), stub2.getChannel());
    }

}
