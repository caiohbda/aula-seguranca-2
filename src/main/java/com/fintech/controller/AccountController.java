package com.fintech.controller;

import com.fintech.dto.AccountRequest;
import com.fintech.dto.AccountResponse;
import com.fintech.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String cpf,
                                                      Authentication authentication) {
        String authenticatedCpf = authentication.getName();
        if (!authenticatedCpf.equals(cpf)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        AccountResponse response = accountService.getAccountByCpf(cpf);
        return ResponseEntity.ok(response);
    }
}
