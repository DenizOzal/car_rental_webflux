package com.car_rental_webflux.service;

import com.car_rental_webflux.model.Car;
import com.car_rental_webflux.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public Flux<Car> findAll(){
        return carRepository.findAll();
    }
    public Mono<Car> save(Car car){
        return carRepository.save(car);
    }

    public Mono<Car> findById(Integer carId){
        return carRepository.findById(carId);
    }

    public Mono<Void> deleteById(Integer carId){
        return carRepository.deleteById(carId);
    }
}
