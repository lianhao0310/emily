package com.alice.emily.module.retrofit;

import com.alice.emily.module.retrofit.auth.BasicAuthenticationConfigurer;
import com.alice.emily.module.retrofit.auth.DigestAuthenticationConfigurer;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by lianhao on 2017/3/30.
 */
@Data
@ConfigurationProperties(prefix = "emily.retrofit")
public class RetrofitProperties {

    private Map<String, RetrofitClient> clients;

    @Data
    public static class RetrofitClient {
        @NotNull
        private String baseUrl;
        @NestedConfigurationProperty
        private BasicAuthenticationConfigurer basic;
        @NestedConfigurationProperty
        private DigestAuthenticationConfigurer digest;
    }
}
