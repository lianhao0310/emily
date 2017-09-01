package com.alice.emily.module.resteasy.resource;

import com.alice.emily.module.resteasy.multipart.MultipartBeanParam;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by lianhao on 2017/3/28.
 */
@Log4j2
@Component
@Path("/photo")
public class PhotoResource {

    /**
     * Resteasy determines the charset from the Content-Type associated with a part.
     * If set correctly everything is working out of the box.
     * HTML Spec: http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.2
     * POST /file HTTP/1.1
     * Content-Type: multipart/form-data; boundary=AaB03x
     * --AaB03x
     * Content-Disposition: form-data; name="file"; filename="file1.txt"
     * Content-Type: text/plain; charset=utf-8
     * ... contents of file1.txt ...
     * --AaB03x--
     *
     * @return the content of the input part "file" as text.
     * Hopefully in the same encoding as sent.
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response upload(MultipartFormDataInput input, @Context HttpHeaders headers) throws IOException {
        InputPart file = input.getFormDataMap().get("file").get(0);
        String content = file.getBodyAsString();
        log.info("Received upload file with MediaType {}:\n {}", file.getMediaType(), content);
        return Response.ok(content).header("Content-Type", file.getMediaType().toString()).build();
    }

    @POST
    @Path("mvc")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadMvc(@MultipartBeanParam PhotoParam param) throws IOException {
        MultipartFile file = param.getFile();
        log.info("Received upload file: {} : {}", param.getDescription(), file);
        return Response.ok().build();
    }

}
