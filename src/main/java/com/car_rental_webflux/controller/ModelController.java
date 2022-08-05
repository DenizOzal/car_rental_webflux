package com.car_rental_webflux.controller;


import com.car_rental_webflux.model.Car;
import com.car_rental_webflux.model.Model;
import com.car_rental_webflux.request.CarRequest;
import com.car_rental_webflux.request.ModelRequest;
import com.car_rental_webflux.response.CustomResponseEntity;
import com.car_rental_webflux.response.ResponseHandler;
import com.car_rental_webflux.service.ModelService;
import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RestController
public class ModelController {
    @Autowired
    private ModelService modelService;

    // Admin can see all models
    @GetMapping("/model")
    @PreAuthorize("hasRole('ADMIN')")
    Flux<CustomResponseEntity<List<Model>>> getAll() {
        return modelService.findAll().
            collectList().flatMapMany(
            response ->
            {
                if(response.isEmpty()){
                    return Flux.just(new CustomResponseEntity<>(1, "Model is not found", null));
                }
                else{
                    return Flux.just(new CustomResponseEntity<>(0, "Success", response));
                }
            });
    }

    // Admin can add a model
    @PostMapping("/model")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<CustomResponseEntity<Model>> addModel(@RequestBody ModelRequest modelRequest) {
        Model model = new Model(modelRequest.getBrand_name(),modelRequest.getModel_name(),modelRequest.getModel_year());
        return modelService.save(model)
                .map(response -> new CustomResponseEntity<>(0,"Success",model))
                .onErrorReturn(new CustomResponseEntity<>(2,"Model is already defined",model));
    }

    // Admin can remove a model
    @DeleteMapping("/model/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<CustomResponseEntity<Model>> deleteById(@PathVariable("id") Integer id) {
         return modelService.findById(id).flatMap(p ->
                 modelService.deleteById(p.getModel_id()).then(Mono.just(new CustomResponseEntity<>(0,"Success",p)))
         ).switchIfEmpty(Mono.just(new CustomResponseEntity<>(1,"Model is not found",null)));
    }

    // Admin can update a model
    @PutMapping("/model/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    Mono<CustomResponseEntity<Model>> updateModel(@PathVariable("id") Integer id, @RequestBody ModelRequest modelRequest) {
        return modelService.findById(id).flatMap(model -> {
            model.setModel_name(modelRequest.getModel_name());
            model.setModel_year(modelRequest.getModel_year());
            model.setBrand_name(modelRequest.getBrand_name());
            return modelService.save(model).map(response -> new CustomResponseEntity<>(0,"Success",model));
        }).switchIfEmpty(Mono.just(new CustomResponseEntity<>(1,"Model is not found",null)));
    }
}
