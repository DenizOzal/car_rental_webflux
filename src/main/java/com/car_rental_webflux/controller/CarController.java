package com.car_rental_webflux.controller;

import com.car_rental_webflux.model.Car;
import com.car_rental_webflux.model.Model;
import com.car_rental_webflux.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CarController {
    @Autowired
    private CarService carService;

    // Admin can see all cars
    @GetMapping("/car")
    @PreAuthorize("hasRole('ADMIN')")
    Flux<Car> getAll() {
        return carService.findAll();
    }

    // Admin can add a car
    @PostMapping("/car")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<Car> addCar(@RequestBody Car car) {
        return carService.save(car);
    }

    // Admin can remove a car
    @DeleteMapping("/car/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<Void> deleteById(@PathVariable("id") Integer id) {
        return carService.findById(id).flatMap(p ->
                carService.deleteById(p.getCar_id()));
    }

    @PutMapping("/car/{id}")
    private Mono<Car> updateCar(@PathVariable("id") Integer id,
                                  @RequestBody Car car) {
        return carService.findById(id).flatMap(car1 -> {
            car1.setCar_id(id);
            return carService.save(car1);
        }).switchIfEmpty(Mono.empty());
    }
}
