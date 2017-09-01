package com.alice.emily.module.resteasy.resource;

import com.alice.emily.utils.LOG;
import com.alice.emily.utils.logging.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by lianhao on 2016/12/6.
 */
@Component
@Provider
public class TestFilter implements ContainerRequestFilter {

    private static final Logger log = LOG.getLogger(TestFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.info("***** Test Filter: {}", requestContext.getUriInfo().getPath());
    }
}
