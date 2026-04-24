package com.fintech.controller;

import com.fintech.dto.TransactionResponse;
import com.fintech.dto.TransferRequest;
import com.fintech.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request,
                                                        Authentication authentication) {
        String authenticatedCpf = authentication.getName();
        TransactionResponse response = transactionService.transfer(request, authenticatedCpf);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/transactions/{cpf}")
    public ResponseEntity<List<TransactionResponse>> getHistory(@PathVariable String cpf,
                                                                Authentication authentication) {
        String authenticatedCpf = authentication.getName();
        List<TransactionResponse> history = transactionService.getHistory(cpf, authenticatedCpf);
        return ResponseEntity.ok(history);
    }
}
