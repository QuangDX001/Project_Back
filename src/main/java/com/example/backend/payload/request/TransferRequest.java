package com.example.backend.payload.request;

import lombok.Data;

/**
 * Created by Admin on 10/25/2023
 */
@Data
public class TransferRequest {
    private long from;
    private long to;
    private long amount;
}
