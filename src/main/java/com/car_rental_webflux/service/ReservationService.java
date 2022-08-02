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
    public Flux<Reservation> getAllReservationUser(Integer user_id){return reservationRepository.getAllReservationUser(user_id);}

    public Mono<Reservation> save(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    public Mono<Reservation> findById(Integer reservationId){
        return reservationRepository.findById(reservationId);
    }

    public Flux<Reservation> findByUserReservation(Integer reservation_id, Integer user_id){
        return reservationRepository.findByUserReservation(reservation_id,user_id);
    }
    public Mono<Void> deleteById(Integer reservationId){
        return reservationRepository.deleteById(reservationId);
    }
    public Flux<Reservation> findReservationSlot(LocalDateTime reservation_start, LocalDateTime reservation_end) {
        return reservationRepository.findReservationSlot(reservation_start,reservation_end);
    }
}
