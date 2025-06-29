package com.hsbc.transaction.handler;

import com.hsbc.transaction.entity.Transaction;
import com.hsbc.transaction.model.TransactionRequest;
import com.hsbc.transaction.model.TransactionType;

public interface TransactionHandler {
    boolean shouldHandle(TransactionType transactionType);
    Transaction initializeTransaction(TransactionRequest request);
    void handle(Transaction transaction);
}
