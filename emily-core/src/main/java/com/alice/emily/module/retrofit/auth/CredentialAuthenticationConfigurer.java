package com.alice.emily.module.retrofit.auth;

import lombok.Data;

/**
 * Created by liupin on 2017/4/1.
 */
@Data
public abstract class CredentialAuthenticationConfigurer implements AuthenticationConfigurer {

    protected String password;
    protected String username;

}
