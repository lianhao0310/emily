package com.alice.emily.module.security.jwt.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by stephan on 20.03.16.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JWTAuthenticationResponse implements Serializable {

    private String token;
}
