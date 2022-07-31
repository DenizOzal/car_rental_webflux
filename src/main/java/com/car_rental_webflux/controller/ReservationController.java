package com.car_rental_webflux.controller;

import com.car_rental_webflux.DemoApplication;
import com.car_rental_webflux.auth.ReservationRequest;
import com.car_rental_webflux.model.Reservation;
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

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

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
    Flux<Reservation> getAll() {
        return reservationService.findAll();
    }

    @PostMapping("/reservation")
    @PreAuthorize("hasRole('USER')")
    Mono<Reservation> addReservation(@RequestBody ReservationRequest reservationRequest) {
        int i = 0;

        String token = (String) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return userService.findByUsername(jwtUtil.getUsernameFromToken(token)).flatMap(
                user -> {
                    LOG.info("Selam1: " );
                    Integer user_id = user.getUser_id();
                    LOG.info("Selam2: " );
                    return reservationService.findByCarId(reservationRequest.getCarId()).flatMap(
                            reservation -> {
                                AtomicInteger j = new AtomicInteger(0);
                                for(LocalDateTime date = reservationRequest.getReservation_start(); !date.isAfter(reservationRequest.getReservation_end()); date = date.plusDays(1)){
                                     reservationService.findReservationSlot(date)
                                            .flatMap(temp -> {
                                                temp.getUser_id();
                                                j.getAndIncrement();
                                                return Mono.error(new Error("Reservation slot is not available"));
                                            });
                                }
                                return reservationService.save(new Reservation(reservation.getUser_id(),reservationRequest.getCarId(), reservationRequest.getReservation_start(),reservationRequest.getReservation_end())).log("selam");
                                //return Mono.just(reservationService.save(new Reservation(reservation.getUser_id(),reservation.getCarId(), reservationRequest.getReservation_start(),reservationRequest.getReservation_end())));
                            }
                    ).switchIfEmpty(Mono.error(new Error("Car is not available")));
                            /*.flatMap(
                                    reservation -> {
                                        return reservationService.save(new Reservation(reservation.getUser_id(),reservation.getCarId(), reservationRequest.getReservation_start(),reservationRequest.getReservation_end()));
                                    }
                            );*/
                });
     /*   if(!reservationService.findByCarId(reservation.getCar_id()).equals(null)){
            Mono<Reservation> temp =  reservationService.findByCarId(reservation.getCar_id());
            LOG.info("LOOOL: " + temp.map(reservation1 -> reservation1.getReservation_id()).toString());
            return Mono.error(new Error("Car has already been taken"));
        }*/

       /* for (LocalDateTime date = reservation.getReservation_start(); !date.isAfter(reservation.getReservation_end()); date = date.plusDays(1))
        {
            if(!reservationService.findReservationSlot(date).equals(null)){
                i++;
            }
           *//* i++;
            LOG.info("i: " + i);*//*
        }*/
       /* if(i == 0){
            return reservationService.save(new Reservation());
        }*/
    }

    // Admin and User can remove a reservation
    @DeleteMapping("/reservation/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    Mono<Void> deleteById(@PathVariable("id") Integer id) {
        return reservationService.findById(id).flatMap(p ->
                reservationService.deleteById(p.getReservation_id()));
    }
}
