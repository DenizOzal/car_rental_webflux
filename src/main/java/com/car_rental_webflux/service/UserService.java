package com.car_rental_webflux.service;

import com.car_rental_webflux.model.Model;
import com.car_rental_webflux.model.User;

import com.car_rental_webflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public Mono<User> findById(Integer userId){
        return userRepository.findById(userId);
    }

    public Mono<Void> deleteById(Integer userId){
        return userRepository.deleteById(userId);
    }
}
