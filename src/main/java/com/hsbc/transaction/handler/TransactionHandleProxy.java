package com.hsbc.transaction.handler;

import com.hsbc.transaction.cache.TransactionCachedRepository;
import com.hsbc.transaction.entity.Transaction;
import com.hsbc.transaction.exception.TransactionNotSupported;
import com.hsbc.transaction.model.TransactionRequest;
import com.hsbc.transaction.model.TransactionResponse;
import com.hsbc.transaction.model.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TransactionHandleProxy {
    private final List<TransactionHandler> transactionHandlers;
    private final TransactionCachedRepository transactionCachedRepository;

    public TransactionHandleProxy(List<TransactionHandler> transactionHandlers, TransactionCachedRepository transactionCachedRepository) {
        this.transactionHandlers = transactionHandlers;
        this.transactionCachedRepository = transactionCachedRepository;
    }

    public TransactionResponse handleTransactionRequest(TransactionRequest request) {
        for (TransactionHandler transactionHandler : transactionHandlers) {
            if(transactionHandler.shouldHandle(TransactionType.valueOf(request.transactionType()))) {
                Transaction constructTransaction = transactionHandler.initializeTransaction(request);
                transactionCachedRepository.updateTransaction(constructTransaction);
                transactionHandler.handle(constructTransaction);
                return TransactionResponse.fromTransaction(constructTransaction);
            }
        }
        log.warn("No transaction handler found for type {}", request.transactionType());
        throw new TransactionNotSupported("Transaction is not supported");
    }
}
