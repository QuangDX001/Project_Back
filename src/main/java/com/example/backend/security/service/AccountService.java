package com.example.backend.security.service;

import com.example.backend.exception.BankTransactionException;

/**
 * Created by Admin on 10/25/2023
 */
public interface AccountService {
    void transfer(long fromAccId, long toAccId, double amount) throws BankTransactionException;
}
