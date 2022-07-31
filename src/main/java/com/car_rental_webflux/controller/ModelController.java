package com.car_rental_webflux.controller;


import com.car_rental_webflux.model.Car;
import com.car_rental_webflux.model.Model;
import com.car_rental_webflux.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ModelController {
    @Autowired
    private ModelService modelService;

    @GetMapping("/model")
    @PreAuthorize("hasRole('ADMIN')")
    Flux<Model> getAll() {
        return modelService.findAll();
    }

    // Admin can add a car
    @PostMapping("/model")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<Model> addModel(@RequestBody Model model) {
        return modelService.save(model);
    }

    // Admin can remove a car
    @DeleteMapping("/model/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<Void> deleteById(@PathVariable("id") Integer id) {
        return modelService.findById(id).flatMap(p ->
                modelService.deleteById(p.getModel_id()));
    }
}
