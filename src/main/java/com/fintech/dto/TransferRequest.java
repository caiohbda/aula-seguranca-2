package com.fintech.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class TransferRequest {

    @NotBlank(message = "Sender CPF is required")
    @Pattern(regexp = "\\d{11}", message = "fromCpf must contain exactly 11 digits")
    private String fromCpf;

    @NotBlank(message = "Receiver CPF is required")
    @Pattern(regexp = "\\d{11}", message = "toCpf must contain exactly 11 digits")
    private String toCpf;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    private String description;

    private String password;

    public TransferRequest() {}

    public String getFromCpf() { return fromCpf; }
    public void setFromCpf(String fromCpf) { this.fromCpf = fromCpf; }

    public String getToCpf() { return toCpf; }
    public void setToCpf(String toCpf) { this.toCpf = toCpf; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
