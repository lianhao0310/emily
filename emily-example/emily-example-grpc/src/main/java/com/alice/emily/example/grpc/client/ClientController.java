package com.alice.emily.example.grpc.client;

import com.alice.emily.module.grpc.GRpcClient;
import com.alice.emily.utils.LOG;
import com.alice.emily.utils.logging.Logger;
import io.grpc.StatusRuntimeException;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


/**
 * Created by Lianhao on 2017/8/17.
 */
@Path("/client")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class ClientController {
    private static final Logger logger = LOG.getLogger(ClientController.class);

    @GRpcClient("pop")
    private GreeterGrpc.GreeterBlockingStub stub;

    @GET
    public void getMessage(@QueryParam("name") String name) {
        logger.info("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = stub.sayHello(request);
            logger.info("Greeting: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            logger.error("RPC failed: {0}", e.getStatus());
        }
    }
}
