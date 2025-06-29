package com.hsbc.transaction.handler;

import com.hsbc.transaction.cache.AccountCachedRepository;
import com.hsbc.transaction.cache.TransactionCachedRepository;
import com.hsbc.transaction.entity.Account;
import com.hsbc.transaction.entity.Transaction;
import com.hsbc.transaction.exception.TransactionExecutionException;
import com.hsbc.transaction.exception.TransactionNotSupported;
import com.hsbc.transaction.model.TransactionRequest;
import com.hsbc.transaction.model.TransactionStatus;
import com.hsbc.transaction.model.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Slf4j
public class WithdrawalHandler implements TransactionHandler {
    private final AccountCachedRepository accountCachedRepository;
    private final TransactionCachedRepository transactionCachedRepository;

    public WithdrawalHandler(AccountCachedRepository accountCachedRepository, TransactionCachedRepository transactionCachedRepository) {
        this.accountCachedRepository = accountCachedRepository;
        this.transactionCachedRepository = transactionCachedRepository;
    }


    @Override
    public boolean shouldHandle(TransactionType transactionType) {
        return transactionType == TransactionType.WITHDRAWAL;
    }

    @Override
    public Transaction initializeTransaction(TransactionRequest request) {
        String debitAccountId = request.debitAccountId();
        if(debitAccountId == null) {
            log.warn("Transaction id: {}, withdrawal requires debit account", request.transactionId());
            throw new TransactionNotSupported("Withdrawal requires debit account");
        }
        Account debitAccount = accountCachedRepository.getAccount(request.debitAccountId());
        if (!debitAccount.isActive()) {
            log.warn("Transaction id: {}, debit account is not active", request.transactionId());
            throw new TransactionNotSupported("Withdrawal debit account is not active");
        }
        if(debitAccount.getBalance().compareTo(request.amount()) < 0) {
            log.warn("Transaction id: {}, insufficient balance in debit account", request.transactionId());
            throw new TransactionNotSupported("Insufficient balance in debit account");
        }
        Transaction transaction = new Transaction();
        transaction.setId(request.transactionId());
        transaction.setAmount(request.amount());
        transaction.setDebitAccountId(debitAccount);
        transaction.setStatus(TransactionStatus.PROCESSING.getCode());
        transaction.setDescription(request.description());
        transaction.setLastUpdateTime(LocalDateTime.now());
        transaction.setType(request.transactionType());
        return transaction;
    }

    @Async("asyncTaskExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(Transaction transaction) {
        try {
            Account debitAccount = transaction.getDebitAccountId();
            debitAccount.setBalance(debitAccount.getBalance().subtract(transaction.getAmount()));
            accountCachedRepository.updateAccount(debitAccount);
            transaction.setStatus(TransactionStatus.SUCCESS.getCode());
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED.getCode());
            transaction.setMessage("Deposit failed");
            log.error("Deposit failed for transaction {}", transaction.getId(), e);
            throw new TransactionExecutionException("Deposit failed");
        }
        transactionCachedRepository.updateTransaction(transaction);
    }
}
