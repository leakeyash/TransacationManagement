package com.hsbc.transaction.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotBlank String transactionId,
        @Positive @DecimalMin("0.01") BigDecimal amount,
        String debitAccountId,
        String creditAccountId,
        @NotBlank String transactionType,
        String description){
}
