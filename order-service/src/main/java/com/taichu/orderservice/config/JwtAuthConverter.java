package com.taichu.orderservice.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.Optional;

import org.springframework.core.convert.converter.Converter;

// public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken>, Converter<Jwt, Collection<GrantedAuthority>> {

//     @Override
//     public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
//         Collection<GrantedAuthority> authorities = extractRoles(jwt);
//         return new JwtAuthenticationToken(jwt, authorities);
//     }

//     private List<GrantedAuthority> extractRoles(Jwt jwt) {
//         Map<String, Object> realmAccess = jwt.getClaim("realm_access");
//         List<String> roles = List.of();
//         if (realmAccess != null && realmAccess.get("roles") instanceof List<?>) {
//             roles = ((List<?>) realmAccess.get("roles")).stream()
//                     .filter(String.class::isInstance)
//                     .map(String.class::cast)
//                     .toList();
//         }

//         return roles.stream()
//                 .map(role -> "ROLE_" + role.toUpperCase())
//                 .map(SimpleGrantedAuthority::new)
//                 .collect(Collectors.toList());
//     }
// }

public class JwtAuthConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<String> roles = Optional.ofNullable(jwt.getClaim("realm_access"))
            .map(claim -> (Map<String, Object>) claim)
            .map(realm -> (List<String>) realm.get("roles"))
            .orElse(Collections.emptyList());

        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
            .collect(Collectors.toList());
    }
}


