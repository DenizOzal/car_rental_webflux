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
@Table("model")
public class Model {
    public Model(String brand_name, String model_name, String model_year) {
        this.brand_name = brand_name;
        this.model_name = model_name;
        this.model_year = model_year;
    }

    @Column("model_id")
    @Id
    private Integer model_id;

    @Column("brand_name")
    private String brand_name;

    @Column("model_name")
    private String model_name;

    @Column("model_year")
    private String model_year;
}
