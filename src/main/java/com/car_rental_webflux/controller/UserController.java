package com.car_rental_webflux.controller;

import com.car_rental_webflux.auth.PasswordChecking;
import com.car_rental_webflux.model.User;
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
    Mono<User> addUser(@RequestBody User user) {
        return userService.save(new User(user.getUsername(),passwordEncoder.encode(user.getPassword()),user.getRoles()));
    }

    // Admin can remove a car
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<Void> deleteById(@PathVariable("id") Integer id) {
        return userService.findById(id).flatMap(p ->
                userService.deleteById(p.getUser_id()));
    }

    // User can change password
    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('USER')")
    Mono<User> updateUser(@RequestBody PasswordChecking passwordChecking) {
        String token = (String) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return userService.findByUsername(jwtUtil.getUsernameFromToken(token))
                .flatMap(userDetails -> {
                    if (!passwordEncoder.encode(passwordChecking.getPassword()).equals(userDetails.getPassword())){
                        return Mono.error(new AuthenticationCredentialsNotFoundException("Your password is not true"));
                    }
                    else if(passwordEncoder.encode(passwordChecking.getPassword()).equals(passwordEncoder.encode(passwordChecking.getNewPassword()))){
                        return Mono.error(new AuthenticationCredentialsNotFoundException("Your password cannot be same as your old password"));
                    }
                    userDetails.setPassword(passwordEncoder.encode(passwordChecking.getNewPassword()));
                    return userService.save(userDetails);
                });
        //if(user.getUsername().equals(jwtUtil.getUsernameFromToken(token)))
    }
}
