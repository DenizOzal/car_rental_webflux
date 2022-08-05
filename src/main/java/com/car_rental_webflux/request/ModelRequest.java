package com.car_rental_webflux.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModelRequest {
    private String brand_name;
    private String model_name;
    private String model_year;
}
