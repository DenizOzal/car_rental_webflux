package com.car_rental_webflux.service;

import com.car_rental_webflux.model.User;

import com.car_rental_webflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Mono<User> save(User user){
        return userRepository.save(user);
    }
}
