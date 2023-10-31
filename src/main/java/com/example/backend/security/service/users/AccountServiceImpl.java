package com.example.backend.security.service.users;

import com.example.backend.exception.BankTransactionException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.Account;
import com.example.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Admin on 10/25/2023
 */
@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void transfer(long fromAccId, long toAccId, double amount) throws BankTransactionException {
        Account fromAccount;
        Account toAccount;

        try {
            fromAccount = getById(fromAccId);
            toAccount = getById(toAccId);
        }catch (ResourceNotFoundException e){
            throw new BankTransactionException("Account not found");
        }

        if(fromAccount.getBalance() < amount){
            throw new BankTransactionException("Not enough balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    public Account getById(long id) throws ResourceNotFoundException{
        Optional<Account> acc = accountRepository.findById(id);
        if(acc.isPresent()){
            Account account = acc.get();
            return account;
        } else {
            throw new ResourceNotFoundException("Account id " + id + "doesn't exist");
        }
    }
}
