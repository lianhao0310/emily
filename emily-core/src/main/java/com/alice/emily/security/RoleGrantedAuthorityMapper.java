package com.alice.emily.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.Set;

/**
 * Created by lianhao on 2017/4/18.
 */
public interface RoleGrantedAuthorityMapper extends GrantedAuthoritiesMapper {

    GrantedAuthority mapRole(String role);

    Set<GrantedAuthority> mapRoles(Collection<String> roles);
}
