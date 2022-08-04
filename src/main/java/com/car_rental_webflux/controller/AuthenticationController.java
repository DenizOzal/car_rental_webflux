package com.car_rental_webflux.controller;


import com.car_rental_webflux.model.User;
import com.car_rental_webflux.security.JWTUtil;
import com.car_rental_webflux.security.PBKDF2Encoder;
import com.car_rental_webflux.request.AuthRequest;
import com.car_rental_webflux.response.AuthResponse;
import com.car_rental_webflux.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class AuthenticationController {
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
        //User user = new User(999,"koray","koray",true,Role.ROLE_USER);
        //userService.save(user).subscribe();
        return userService.findByUsername(ar.getUsername())
            .filter(userDetails -> {
                return passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword());
            })
            .map(userDetails -> {
                return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
                 })
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity> signup(@RequestBody User user) {
        return Mono.just(ResponseEntity
                        .ok()
                        .body(userService.save(new User(user.getUsername(),passwordEncoder.encode(user.getPassword()),user.getRoles())).subscribe()));

    }
}
