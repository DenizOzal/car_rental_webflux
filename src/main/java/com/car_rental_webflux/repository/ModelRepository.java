package com.car_rental_webflux.repository;

import com.car_rental_webflux.model.Model;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ModelRepository extends ReactiveCrudRepository<Model, Integer> {

}