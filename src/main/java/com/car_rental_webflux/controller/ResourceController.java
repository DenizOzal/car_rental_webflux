package com.car_rental_webflux.controller;

import com.car_rental_webflux.response.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ResourceController {

    @GetMapping("/resource/user")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<Message>> user() {
        return Mono.just(ResponseEntity.ok(new Message("Content for user")));
    }

    @GetMapping("/resource/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Message>> admin() {
        return Mono.just(ResponseEntity.ok(new Message("Content for admin")));
    }

    @GetMapping("/resource/user-or-admin")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Message>> userOrAdmin() {
        return Mono.just(ResponseEntity.ok(new Message("Content for user or admin")));
    }
}
