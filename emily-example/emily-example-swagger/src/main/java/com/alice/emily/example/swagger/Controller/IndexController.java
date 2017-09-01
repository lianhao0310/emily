package com.alice.emily.example.swagger.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * Created by Lianhao on 2017/9/1.
 */
@Api(tags = "index")
@Validated
@RestController
@RequestMapping("index")
public class IndexController {
    @ApiOperation("登录")
    @PostMapping("{name}")
    public String login(@PathVariable @NotNull String name) {
        return name;
    }

}
