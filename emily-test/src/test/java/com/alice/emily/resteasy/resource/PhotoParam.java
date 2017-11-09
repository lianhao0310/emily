package com.alice.emily.resteasy.resource;

import com.alice.emily.resteasy.multipart.MultipartParam;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.FormParam;

/**
 * Created by lianhao on 2017/3/29.
 */
@Data
public class PhotoParam {

    @FormParam("version")
    private Integer version;

    @FormParam("desc")
    private String description;

    @MultipartParam("file")
    private MultipartFile file;
}
