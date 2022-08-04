package com.car_rental_webflux.repository;

import com.car_rental_webflux.model.Reservation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends ReactiveCrudRepository<Reservation, Integer> {
    @Query("CALL my_proc(:reservation_start,:reservation_end)")
    Flux<Reservation> findReservationSlot(@Param("reservation_start") LocalDateTime reservation_start, @Param("reservation_end") LocalDateTime reservation_end);
    Mono<Reservation> save(Reservation reservation);

    @Query("SELECT * FROM reservation WHERE reservation_id = :reservation_id AND user_id = :user_id")
    Flux<Reservation> findByUserReservation(@Param("reservation_id") Integer reservation_id, @Param("user_id") Integer user_id);

    @Query("SELECT * FROM reservation WHERE user_id = :user_id")
    Flux<Reservation> getAllReservationUser(@Param("user_id") Integer user_id);
}