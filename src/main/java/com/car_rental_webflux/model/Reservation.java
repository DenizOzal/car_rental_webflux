package com.car_rental_webflux.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Basic;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("reservation")
public class Reservation {

    @Column("reservation_id")
    @Id
    private Integer reservation_id;

    @Column("user_id")
    private Integer user_id;

    public Reservation(Integer user_id, Integer carId, LocalDateTime reservation_start, LocalDateTime reservation_end) {
        this.user_id = user_id;
        this.carId = carId;
        this.reservation_start = reservation_start;
        this.reservation_end = reservation_end;
    }

    @Column("car_id")
    private Integer carId;

    @Column("reservation_start")
    private LocalDateTime reservation_start;

    @Column("reservation_end")
    private LocalDateTime reservation_end;
}
