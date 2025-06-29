package com.hsbc.transaction.model;

import com.hsbc.transaction.entity.Account;
import com.hsbc.transaction.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public record TransactionResponse(
    String transactionId,
    String debitAccountId,
    String creditAccountId,
    String transactionType,
    BigDecimal amount,
    String description,
    LocalDateTime lastUpdateTime,
    String status,
    String message) {
  public static TransactionResponse fromTransaction(Transaction transaction) {
    return new TransactionResponse(
        transaction.getId(),
            Optional.ofNullable(transaction.getDebitAccountId()).map(Account::getId).orElse(null),
            Optional.ofNullable(transaction.getCreditAccountId()).map(Account::getId).orElse(null),
        transaction.getType(),
        transaction.getAmount(),
        transaction.getDescription(),
        transaction.getLastUpdateTime(),
        transaction.getStatus(),
        transaction.getMessage());
  }
}
