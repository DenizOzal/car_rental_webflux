package com.car_rental_webflux.service;

import com.car_rental_webflux.model.Reservation;
import com.car_rental_webflux.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Flux<Reservation> findAll(){
        return reservationRepository.findAll();
    }
    public Mono<Reservation> save(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    public Mono<Reservation> findById(Integer reservationId){
        return reservationRepository.findById(reservationId);
    }

    public Mono<Void> deleteById(Integer reservationId){
        return reservationRepository.deleteById(reservationId);
    }
    public Flux<Reservation> findReservationSlot(LocalDateTime date) {
        return reservationRepository.findReservationSlot(date);
    }

    public Mono<Reservation> findByCarId(Integer carId){
        return reservationRepository.findByCarId(carId);
    }

}
