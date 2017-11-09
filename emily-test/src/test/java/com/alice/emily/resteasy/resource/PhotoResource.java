package com.alice.emily.resteasy.resource;

import com.google.common.collect.Lists;
import com.alice.emily.resteasy.multipart.MultipartBeanParam;
import com.alice.emily.resteasy.multipart.MultipartParam;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by lianhao on 2017/3/28.
 */
@Log4j2
@Component
@Path("/photo")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.TEXT_PLAIN)
public class PhotoResource {

    @POST
    public Response upload(@MultipartParam("file") MultipartFile input,
                           @Context HttpHeaders headers) throws IOException {
        String content = IOUtils.toString(input.getInputStream(), StandardCharsets.UTF_8);
        log.info("Received upload file with MediaType {}:\n {}", input.getContentType(), content);
        return Response.ok(content).header("Content-Type", input.getContentType()).build();
    }

    @POST
    @Path("part")
    public Response uploadPart(@MultipartParam("file") Part part) throws IOException {
        String content = IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8);
        log.info("Received upload file with MediaType {}:\n {}", part.getContentType(), content);
        return Response.ok(content).header("Content-Type", part.getContentType()).build();
    }

    @POST
    @Path("mvc")
    public Response uploadMvc(@QueryParam("name") String name,
                              @MultipartBeanParam PhotoParam param) throws IOException {
        log.info("Received photo: {} {}", name, param.getVersion());
        MultipartFile file = param.getFile();
        log.info("Received upload file: {} : {}", param.getDescription(), file);
        return Response.ok().build();
    }

    @GET
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Page<String> list(Pageable pageable) {
        log.info("Received pageable parameter: {}", pageable);
        int size = pageable.getPageSize();
        List<String> data = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            data.add("Page item " + i);
        }
        return new PageImpl<>(data, pageable, Integer.MAX_VALUE);
    }
}
