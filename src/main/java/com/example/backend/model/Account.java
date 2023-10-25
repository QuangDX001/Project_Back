package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Created by Admin on 10/25/2023
 */
@Data
@Entity
@Table(name = "account")
public class Account {
    @Id
    private long id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    private User user;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private double balance;
}
