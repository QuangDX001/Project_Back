package com.example.backend.payload.dto.user;

import lombok.Data;

/**
 * Created by Admin on 10/16/2023
 */
@Data
public class ProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String address;
    private String phone;
    private double balance;
    private String firstName;
    private String lastName;
}
