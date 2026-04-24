package com.fintech.service;

import com.fintech.dto.AccountRequest;
import com.fintech.dto.AccountResponse;
import com.fintech.model.Account;
import com.fintech.repository.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        if (accountRepository.existsByCpf(request.getCpf())) {
            throw new IllegalArgumentException("An account with this CPF already exists");
        }
        Account account = new Account(
                request.getName(),
                request.getCpf(),
                passwordEncoder.encode(request.getPassword())
        );
        Account saved = accountRepository.save(account);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public boolean verifyPassword(String cpf, String rawPassword) {
        Account account = accountRepository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for CPF: " + cpf));
        return passwordEncoder.matches(rawPassword, account.getPassword());
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountByCpf(String cpf) {
        Account account = accountRepository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for CPF: " + cpf));
        return toResponse(account);
    }

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        Account account = accountRepository.findByCpf(cpf)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found for CPF: " + cpf));
        return User.builder()
                .username(account.getCpf())
                .password(account.getPassword())
                .roles("USER")
                .build();
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getCpf(),
                account.getBalance()
        );
    }
}
