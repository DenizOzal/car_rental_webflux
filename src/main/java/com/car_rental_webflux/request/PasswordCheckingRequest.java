package com.car_rental_webflux.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswordCheckingRequest {
    private String newPassword;
    private String password;
}
