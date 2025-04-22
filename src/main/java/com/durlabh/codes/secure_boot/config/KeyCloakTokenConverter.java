package com.durlabh.codes.secure_boot.config;

import com.nimbusds.jwt.JWT;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyCloakTokenConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> claims = (Map<String, Object>) source.getClaims().get("realm_access");
        if(claims == null || claims.isEmpty()) {
            return new ArrayList<>();
        }

        return ((List<String>)claims.get("roles"))
                .stream()
                .map(role -> "ROLE_"+role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
