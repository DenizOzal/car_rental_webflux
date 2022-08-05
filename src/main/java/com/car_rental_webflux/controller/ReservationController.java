package com.car_rental_webflux.controller;

import com.car_rental_webflux.DemoApplication;
import com.car_rental_webflux.request.ReservationRequest;
import com.car_rental_webflux.model.Reservation;
import com.car_rental_webflux.response.CustomResponseEntity;
import com.car_rental_webflux.security.JWTUtil;
import com.car_rental_webflux.service.ReservationService;
import com.car_rental_webflux.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ReservationController {
    private static Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("/reservation")
    @PreAuthorize("hasRole('ADMIN')")
    Flux<CustomResponseEntity<List<Reservation>>> getAll() {
        return reservationService.findAll().collectList().flatMapMany(
                response -> {
                    if(response.isEmpty()){
                        return Flux.just(new CustomResponseEntity<>(1, "Reservation is not found", null));
                    }
                    else{
                        return Flux.just(new CustomResponseEntity<>(0, "Success", response));
                    }
                }
        );
    }

    @GetMapping("/reservation-user")
    @PreAuthorize("hasRole('USER')")
    Flux<CustomResponseEntity<?>> getAllReservationUser() {
        String token = (String) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return userService.findByUsername(jwtUtil.getUsernameFromToken(token))
                .flatMapMany(user ->
                {
                    return reservationService.getAllReservationUser(user.getUser_id())
                            .collectList().flatMapMany(response ->
                            {
                                if (response.isEmpty()) {
                                    return Flux.just(new CustomResponseEntity<>(1, "Model is not found", null));
                                } else {
                                    return Flux.just(new CustomResponseEntity<>(0, "Success", response));
                                }
                            });
                }).onErrorReturn(new CustomResponseEntity<>(1,"User is not found",null));
    }

    @PostMapping("/reservation")
    @PreAuthorize("hasRole('USER')")
    Mono<CustomResponseEntity<?>> addReservation(@RequestBody ReservationRequest reservationRequest) {
        String token = (String) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if(reservationRequest.getReservation_start().isEqual(reservationRequest.getReservation_end()) || reservationRequest.getReservation_start().isAfter(reservationRequest.getReservation_end())){
            return Mono.just(new CustomResponseEntity<>(3,"Reservation dates are not true",null));
        }

        return userService.findByUsername(jwtUtil.getUsernameFromToken(token)).flatMap(
                user -> {
                    return reservationService.findReservationSlot(reservationRequest.getReservation_start(),reservationRequest.getReservation_end())
                            .hasElements()
                            .flatMap(
                                    temp -> {
                                        if(temp){
                                            return Mono.just(new CustomResponseEntity<>(1,"Reservations slots are not found",null));
                                        }
                                        else{
                                            Reservation reservation = new Reservation(user.getUser_id(),reservationRequest.getCarId(), reservationRequest.getReservation_start(),reservationRequest.getReservation_end());
                                            return reservationService.save(reservation)
                                                    .map(res -> new CustomResponseEntity<>(0,"Success",res))
                                                    .onErrorReturn(new CustomResponseEntity<>(1,"Car is not found",null));
                                        }
                                    });
                }).onErrorReturn(new CustomResponseEntity<>(1,"User is not found",null));
    }

    // User can remove a reservation
    @DeleteMapping("/reservation-user/{id}")
    @PreAuthorize("hasRole('USER')")
    Mono<CustomResponseEntity<?>> deleteByIdUser(@PathVariable("id") Integer id) {
        String token = (String) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return userService.findByUsername(jwtUtil.getUsernameFromToken(token)).flatMap(
                user -> {
                   return reservationService.findByUserReservation(id,user.getUser_id())
                           .hasElements()
                           .flatMap(
                                   flag -> {
                                       if(flag){
                                           return reservationService.deleteById(id).then(Mono.just(new CustomResponseEntity<>(0,"Success",id)));
                                       }
                                       else
                                       {
                                           return Mono.just(new CustomResponseEntity<>(1,"Reservation is not found",null));
                                       }
                                   }
                           );
                }
        ).onErrorReturn(new CustomResponseEntity<>(1,"User is not found",null));
    }

    // Admin can remove a reservation
    @DeleteMapping("/reservation/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<CustomResponseEntity<Reservation>> deleteById(@PathVariable("id") Integer id) {
        return reservationService.findById(id).flatMap(p ->
                reservationService.deleteById(p.getReservation_id()).then(Mono.just(new CustomResponseEntity<>(0,"Success",p))))
                .switchIfEmpty(Mono.just(new CustomResponseEntity<>(1,"Reservation is not found",null)));
    }
}
