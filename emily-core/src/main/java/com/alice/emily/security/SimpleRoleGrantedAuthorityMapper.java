package com.alice.emily.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lianhao on 2017/4/18.
 */
@Data
public class SimpleRoleGrantedAuthorityMapper implements RoleGrantedAuthorityMapper {

    public enum Case {
        UPPER, LOWER
    }

    private String defaultRole;
    private String prefix = "ROLE_";
    private Case convertCase = Case.UPPER;

    @Override
    public GrantedAuthority mapRole(String role) {
        switch (convertCase) {
            case UPPER:
                role = role.toUpperCase();
                break;
            case LOWER:
            default:
                role = role.toLowerCase();
                break;
        }
        if (prefix.length() > 0 && !role.startsWith(prefix)) {
            role = prefix + role;
        }
        return new SimpleGrantedAuthority(role);
    }

    @Override
    public Set<GrantedAuthority> mapRoles(Collection<String> roles) {
        Set<GrantedAuthority> mapped = new HashSet<>(roles.size());
        for (String role : roles) {
            mapped.add(mapRole(role));
        }
        if (StringUtils.hasLength(defaultRole)) {
            mapped.add(mapRole(defaultRole));
        }
        return mapped;
    }

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> mapped = new HashSet<>(authorities.size());
        for (GrantedAuthority authority : authorities) {
            mapped.add(mapRole(authority.getAuthority()));
        }
        if (StringUtils.hasLength(defaultRole)) {
            mapped.add(mapRole(defaultRole));
        }
        return mapped;
    }
}
