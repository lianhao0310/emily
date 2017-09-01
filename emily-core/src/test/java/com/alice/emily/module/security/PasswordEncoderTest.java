package com.palmaplus.euphoria.module.security;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by liupin on 2017/4/11.
 */
public class PasswordEncoderTest {

    @Test
    public void testBCrypt() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "bcrypt";
        String e1 = encoder.encode(password);
        System.out.println(e1);
        String e2 = encoder.encode(password);
        System.out.println(e2);
        assertThat(e1).isNotEqualTo(e2);
        assertThat(encoder.matches(password, e1)).isTrue();
        assertThat(encoder.matches(password, e2)).isTrue();
    }
}
