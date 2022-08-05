package com.car_rental_webflux.controller;

import com.car_rental_webflux.model.Car;
import com.car_rental_webflux.model.Model;
import com.car_rental_webflux.request.CarRequest;
import com.car_rental_webflux.response.CustomResponseEntity;
import com.car_rental_webflux.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class CarController {
    @Autowired
    private CarService carService;

    // Admin can see all cars
    @GetMapping("/car")
    @PreAuthorize("hasRole('ADMIN')")
    Flux<CustomResponseEntity<List<Car>>> getAll() {
        return carService.findAll().collectList().flatMapMany(
           response -> Flux.just(new CustomResponseEntity<>(0,"Success",response)))
                   .switchIfEmpty(Flux.just(new CustomResponseEntity<>(1,"Car is not found",null)));
    }

    // Admin can add a car
    @PostMapping("/car")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<CustomResponseEntity<Car>> addCar(@RequestBody CarRequest carRequest) {
        Car car = new Car(carRequest.getModel_id(),carRequest.getLicense_number());
        return carService.save(car)
                .map(response -> new CustomResponseEntity<>(0,"Success",car))
                .onErrorReturn(new CustomResponseEntity<>(2,"Car is already defined",car));
    }

    // Admin can remove a car
    @DeleteMapping("/car/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<CustomResponseEntity<Car>> deleteById(@PathVariable("id") Integer id) {
        return carService.findById(id).flatMap(p ->
                carService.deleteById(p.getCar_id()).then(Mono.just(new CustomResponseEntity<>(0,"Success",p))))
                .switchIfEmpty(Mono.just(new CustomResponseEntity<>(1,"Car is not found",null)));
    }


}
