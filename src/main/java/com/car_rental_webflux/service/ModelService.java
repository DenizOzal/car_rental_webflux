package com.car_rental_webflux.service;

import com.car_rental_webflux.model.Model;
import com.car_rental_webflux.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ModelService {

    @Autowired
    private ModelRepository modelRepository;

    public Flux<Model> findAll(){
        return modelRepository.findAll();
    }

    public Mono<Model> save(Model model){
        return modelRepository.save(model);
    }

    public Mono<Model> findById(Integer modelId){
        return modelRepository.findById(modelId);
    }

    public Mono<Void> deleteById(Integer modelId){
        return modelRepository.deleteById(modelId);
    }
}