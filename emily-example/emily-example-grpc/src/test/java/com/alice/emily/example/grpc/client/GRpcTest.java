package com.alice.emily.example.grpc.client;

import com.alice.emily.autoconfigure.GRpcAutoConfiguration;
import com.alice.emily.module.grpc.GRpcClient;
import com.alice.emily.utils.LOG;
import com.alice.emily.utils.logging.Logger;
import io.grpc.StatusRuntimeException;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by lianhao on 2016/12/8.
 */
@SpringBootTest(classes = GRpcAutoConfiguration.class)
public class GRpcTest extends AbstractJUnit4SpringContextTests {
    private static final Logger logger = LOG.getLogger(GRpcTest.class);

    @GRpcClient("pop")
    private GreeterGrpc.GreeterBlockingStub stub1;

    @GRpcClient("pop")
    private GreeterGrpc.GreeterBlockingStub stub2;

    @Test
    public void testClient() {
        Assert.assertNotNull(stub1);
        Assert.assertNotNull(stub2);
        Assert.assertEquals(stub1.getChannel(), stub2.getChannel());

        String name = "mike";
        logger.info("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = stub1.sayHello(request);
            logger.info("Greeting: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            logger.error("RPC failed: {0}", e.getStatus());
        }
    }

}
