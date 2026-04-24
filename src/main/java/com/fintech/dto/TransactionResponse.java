package com.fintech.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private String senderCpf;
    private String senderName;
    private String receiverCpf;
    private String receiverName;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String description;

    public TransactionResponse() {}

    public TransactionResponse(Long id, String senderCpf, String senderName,
                               String receiverCpf, String receiverName,
                               BigDecimal amount, LocalDateTime createdAt, String description) {
        this.id = id;
        this.senderCpf = senderCpf;
        this.senderName = senderName;
        this.receiverCpf = receiverCpf;
        this.receiverName = receiverName;
        this.amount = amount;
        this.createdAt = createdAt;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSenderCpf() { return senderCpf; }
    public void setSenderCpf(String senderCpf) { this.senderCpf = senderCpf; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getReceiverCpf() { return receiverCpf; }
    public void setReceiverCpf(String receiverCpf) { this.receiverCpf = receiverCpf; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
