package com.alice.emily.module.security;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.List;

@Data
public class SecurityConstraint {
    /**
     * The name of your security constraint
     */
    private String name;
    /**
     * The description of your security collection
     */
    private String description;
    /**
     * A list of roles that applies for this security collection
     */
    private List<String> authRoles = Lists.newArrayList();
    /**
     * A list of URL patterns that should match to apply the security collection
     */
    private List<String> patterns = Lists.newArrayList();
    /**
     * A list of HTTP methods that applies for this security collection
     */
    private List<HttpMethod> methods = Lists.newArrayList();
    /**
     * A list of HTTP methods that will be omitted for this security collection
     */
    private List<HttpMethod> omittedMethods = Lists.newArrayList();
}