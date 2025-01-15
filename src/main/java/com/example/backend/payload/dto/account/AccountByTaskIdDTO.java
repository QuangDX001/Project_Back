package com.example.backend.payload.dto.account;

import lombok.Data;

/**
 * Created by Admin on 3/26/2024
 */
@Data
public class AccountByTaskIdDTO {
    private Long id;
    private String username;
    private String email;
    private String address;
    private String phone;

    public AccountByTaskIdDTO(long id, String username, String email, String address, String phone){
        this.id = id;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }
}
