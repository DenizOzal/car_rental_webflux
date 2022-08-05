package com.car_rental_webflux.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponseEntity<T>{
    private Integer status;
    private String message;
    private T detail;
}
