package com.car_rental_webflux.request;

import com.car_rental_webflux.enumaration.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequest {
    private String username;
    private String password;
    private Role roles;
}
