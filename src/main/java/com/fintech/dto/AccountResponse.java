package com.fintech.dto;

import java.math.BigDecimal;

public class AccountResponse {

    private Long id;
    private String name;
    private String cpf;
    private BigDecimal balance;

    public AccountResponse() {}

    public AccountResponse(Long id, String name, String cpf, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
