package com.car_rental_webflux.repository;

import com.car_rental_webflux.model.Reservation;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends ReactiveCrudRepository<Reservation, Integer> {
    @Query("SELECT * FROM reservation WHERE reservation_start <= :reservation OR reservation_end >= :reservat")
    Flux<Reservation> findReservationSlot(@Param("reservation") LocalDateTime reservation);

    @Query("SELECT * FROM reservation WHERE car_id != :carId")
    Mono<Reservation> findByCarId(@Param("carId") Integer carId);

    Mono<Reservation> save(Reservation reservation);
}