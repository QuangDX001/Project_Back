package com.example.backend.controller;

import com.example.backend.exception.BankTransactionException;
import com.example.backend.payload.dto.account.AccountDTO;
import com.example.backend.payload.request.TransferRequest;
import com.example.backend.repository.AccountRepository;
import com.example.backend.security.service.users.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by Admin on 10/25/2023
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/acc")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<AccountDTO> getAllUsers() {
        List<AccountDTO> dto = accountRepository.getUsernameAndBalance2();
        return dto;
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest transferRequest) {
        try {
            accountService.transfer(
                    transferRequest.getFrom(),
                    transferRequest.getTo(),
                    transferRequest.getAmount());
            return ResponseEntity.ok().body("Transfer Success");
        } catch (BankTransactionException e) {
            return ResponseEntity.badRequest().body("Fail");
        }
    }
}
