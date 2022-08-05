package com.car_rental_webflux.response;

import com.car_rental_webflux.model.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponseForDelete(String message, HttpStatus status, Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("model_id", id);
        map.put("message", message);
        map.put("status", status.value());
        return new ResponseEntity<Object>(map,status);
    }
}