package com.palmaplus.euphoria.module.resteasy.resource;

import com.palmaplus.euphoria.utils.LOG;
import com.palmaplus.euphoria.utils.logging.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by liupin on 2016/12/6.
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
