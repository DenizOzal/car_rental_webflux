package com.car_rental_webflux.controller;

import com.car_rental_webflux.request.PasswordCheckingRequest;
import com.car_rental_webflux.model.User;
import com.car_rental_webflux.request.UserRequest;
import com.car_rental_webflux.response.CustomResponseEntity;
import com.car_rental_webflux.security.JWTUtil;
import com.car_rental_webflux.security.PBKDF2Encoder;
import com.car_rental_webflux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;
    // Admin can add a car
    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<CustomResponseEntity<User>> addUser(@RequestBody UserRequest userRequest) {
        User user = new User(userRequest.getUsername(),passwordEncoder.encode(userRequest.getPassword()),userRequest.getRoles());
        return userService.save(user)
                .map(response -> new CustomResponseEntity<>(0,"Success",user))
                .onErrorReturn(new CustomResponseEntity<>(2,"User is already exist",user));
    }

    // Admin can remove a car
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<CustomResponseEntity<User>> deleteById(@PathVariable("id") Integer id) {
        return userService.findById(id).flatMap(p ->
                userService.deleteById(p.getUser_id()).then(Mono.just(new CustomResponseEntity<>(0,"Success",p))))
                .switchIfEmpty(Mono.just(new CustomResponseEntity<>(1,"User is not found",null)));
    }

    // User can change password
    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('USER')")
    Mono<CustomResponseEntity<User>> updateUser(@RequestBody PasswordCheckingRequest passwordCheckingRequest) {
        String token = (String) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return userService.findByUsername(jwtUtil.getUsernameFromToken(token))
                .flatMap(userDetails -> {
                    if (!passwordEncoder.encode(passwordCheckingRequest.getPassword()).equals(userDetails.getPassword())){
                        return Mono.just(new CustomResponseEntity<>(1,"Password is not found",null));
                    }
                    else if(passwordEncoder.encode(passwordCheckingRequest.getPassword()).equals(passwordEncoder.encode(passwordCheckingRequest.getNewPassword()))){
                        return Mono.just(new CustomResponseEntity<>(2,"New password cannot be same as old password",null));
                    }
                    userDetails.setPassword(passwordEncoder.encode(passwordCheckingRequest.getNewPassword()));
                    return userService.save(userDetails).then(Mono.just(new CustomResponseEntity<>(0,"Success",userDetails)))
                            .onErrorReturn(new CustomResponseEntity<>(999,"No idea",null));
                });
    }
}
