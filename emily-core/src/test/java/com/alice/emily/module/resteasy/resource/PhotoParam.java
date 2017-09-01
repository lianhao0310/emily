package com.alice.emily.module.resteasy.resource;

import com.alice.emily.module.resteasy.multipart.MultipartFileParam;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.FormParam;

/**
 * Created by lianhao on 2017/3/29.
 */
@Data
public class PhotoParam {

    @FormParam("desc")
    private String description;

    @MultipartFileParam("file")
    private MultipartFile file;
}
