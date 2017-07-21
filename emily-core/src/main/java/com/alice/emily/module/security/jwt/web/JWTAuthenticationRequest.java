package com.alice.emily.module.security.jwt.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by stephan on 20.03.16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthenticationRequest implements Serializable {

    private String username;
    private String password;
}
