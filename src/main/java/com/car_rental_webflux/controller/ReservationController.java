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

    @GetMapping("/reservation-user")
    @PreAuthorize("hasRole('USER')")
    Flux<Reservation> getAllUser() {
        String token = (String) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return userService.findByUsername(jwtUtil.getUsernameFromToken(token))
                .flatMapMany(user -> reservationService.getAllReservationUser(user.getUser_id()));
    }

    @PostMapping("/reservation")
    @PreAuthorize("hasRole('USER')")
    Mono<Reservation> addReservation(@RequestBody ReservationRequest reservationRequest) {
        String token = (String) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if(reservationRequest.getReservation_start().isEqual(reservationRequest.getReservation_end()) || reservationRequest.getReservation_start().isAfter(reservationRequest.getReservation_end())){
            return Mono.error(new Error("Please check your reservation dates!!!"));
        }

        return userService.findByUsername(jwtUtil.getUsernameFromToken(token)).flatMap(
            user -> {
                return reservationService.findReservationSlot(reservationRequest.getReservation_start(),reservationRequest.getReservation_end())
                        .hasElements()
                        .flatMap(
                            temp -> {
                                if(temp){
                                    return Mono.error(new Error("Reservation slots are not available!!!"));
                                }
                                else{
                                    return reservationService.save(new Reservation(user.getUser_id(),reservationRequest.getCarId(), reservationRequest.getReservation_start(),reservationRequest.getReservation_end()))
                                            .onErrorMap(error -> new Error("Car is not available!!!"));
                                }
                        });
            });
    }

    // User can remove a reservation
    @DeleteMapping("/reservation-user/{id}")
    @PreAuthorize("hasRole('USER')")
    Mono<Void> deleteByIdUser(@PathVariable("id") Integer id) {
        String token = (String) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return userService.findByUsername(jwtUtil.getUsernameFromToken(token)).flatMap(
                user -> {
                   return reservationService.findByUserReservation(id,user.getUser_id())
                           .hasElements()
                           .flatMap(
                                   flag -> {
                                       if(flag){

                                           return reservationService.deleteById(id);
                                       }
                                       else
                                       {
                                           return Mono.error(new Error("Reservation id is not found"));
                                       }
                                   }
                           );
                      /*     .onErrorMap(error -> new Error("1"))
                           .doOnSuccess(success -> ResponseEntity.ok().body("Reservation id:" + id + " is successfully deleted"));*/
                           /*.on(Mono.error(new Error("Reservation id is not true!!!")))
                           .doOnSuccess(success -> ResponseEntity.ok().body("Reservation id:" + id + " is successfully deleted"));*/
                }
        );
    }


    // Admin can remove a reservation
    @DeleteMapping("/reservation/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<Void> deleteById(@PathVariable("id") Integer id) {
        return reservationService.findById(id).flatMap(p ->
                reservationService.deleteById(p.getReservation_id()));
    }
}
