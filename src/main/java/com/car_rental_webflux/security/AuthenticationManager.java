package com.car_rental_webflux.security;

import com.car_rental_webflux.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;
    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = jwtUtil.getUsernameFromToken(authToken);

        return Mono.just(jwtUtil.validateToken(authToken))
            .filter(valid -> valid)
            .switchIfEmpty(Mono.empty())
            .map(valid -> {
                Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
                String rolesMap = claims.get("role", String.class);
                return new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    rolesMap.lines().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );
            });
    }
}
