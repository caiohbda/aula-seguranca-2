package com.fintech.service;

import com.fintech.dto.TransactionResponse;
import com.fintech.dto.TransferRequest;
import com.fintech.model.Account;
import com.fintech.model.Transaction;
import com.fintech.repository.AccountRepository;
import com.fintech.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest request, String authenticatedCpf) {
        if (!authenticatedCpf.equals(request.getFromCpf())) {
            throw new SecurityException("Você só pode transferir dinheiro da sua própria conta");
        }
        if (request.getFromCpf().equals(request.getToCpf())) {
            throw new IllegalArgumentException("A conta de origem e a de destino devem ser diferentes");
        }

        // Lock accounts in consistent order to avoid deadlocks
        String firstCpf  = request.getFromCpf().compareTo(request.getToCpf()) < 0
                ? request.getFromCpf() : request.getToCpf();
        String secondCpf = firstCpf.equals(request.getFromCpf())
                ? request.getToCpf() : request.getFromCpf();

        Account first  = accountRepository.findByCpfWithLock(firstCpf)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada para o CPF: " + firstCpf));
        Account second = accountRepository.findByCpfWithLock(secondCpf)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada para o CPF: " + secondCpf));

        Account sender   = first.getCpf().equals(request.getFromCpf()) ? first : second;
        Account receiver = first.getCpf().equals(request.getToCpf())   ? first : second;

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferência");
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = new Transaction(sender, receiver, request.getAmount(), request.getDescription());
        Transaction saved = transactionRepository.save(transaction);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getHistory(String cpf, String authenticatedCpf) {
        if (!authenticatedCpf.equals(cpf)) {
            throw new SecurityException("You can only view your own transaction history");
        }
        Account account = accountRepository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for CPF: " + cpf));
        return transactionRepository.findAllByAccount(account)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getSender().getCpf(),
                t.getSender().getName(),
                t.getReceiver().getCpf(),
                t.getReceiver().getName(),
                t.getAmount(),
                t.getCreatedAt(),
                t.getDescription()
        );
    }
}
