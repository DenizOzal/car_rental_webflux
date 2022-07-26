package com.car_rental_webflux.repository;

import com.car_rental_webflux.model.Car;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends ReactiveCrudRepository<Car, Integer> {
}