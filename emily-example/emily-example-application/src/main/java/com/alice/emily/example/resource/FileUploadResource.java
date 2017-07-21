package com.alice.emily.example.resource;

import com.alice.emily.module.resteasy.multipart.MultipartFileParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by liupin on 2017/3/28.
 */
@Log4j2
@Component
@Path("fileUpload")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.TEXT_PLAIN)
public class FileUploadResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@MultipartFileParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        log.info("Received upload file with MediaType {}:\n {}", file.getContentType(), content);
        return Response.ok(content).header("Content-Type", file.getContentType()).build();
    }

}
