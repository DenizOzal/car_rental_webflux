package com.car_rental_webflux.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("car")
public class Car {

    @Column("car_id")
    @Id
    private Integer car_id;

    @Column("model_id")
    private Integer model_id;

    @Column("license_number")
    private String license_number;

    public Car(Integer model_id, String license_number) {
        this.model_id = model_id;
        this.license_number = license_number;
    }
}
