package com.example.backend.payload.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;


/**
 * Created by Admin on 10/25/2023
 */
@Data
public class AccountDTO {
    private long id;
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    private double balance;

    public AccountDTO(long id, String username, double balance) {
        this.id = id;
        this.username = username;
        this.balance = balance;
    }
}
