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
public class TransferHandler implements TransactionHandler{
    private final AccountCachedRepository accountCachedRepository;
    private final TransactionCachedRepository transactionCachedRepository;

    public TransferHandler(AccountCachedRepository accountCachedRepository, TransactionCachedRepository transactionCachedRepository) {
        this.accountCachedRepository = accountCachedRepository;
        this.transactionCachedRepository = transactionCachedRepository;
    }

    @Override
    public boolean shouldHandle(TransactionType transactionType) {
        return transactionType == TransactionType.TRANSFER;
    }

    @Override
    public Transaction initializeTransaction(TransactionRequest request) {
        String creditAccountId = request.creditAccountId();
        String debitAccountId = request.debitAccountId();
        if(creditAccountId == null || debitAccountId == null) {
            log.warn("Transaction id: {}, Debit account or credit account is blank", request.transactionId());
            throw new TransactionNotSupported("Debit account or credit account is blank");
        }
        if (creditAccountId.equalsIgnoreCase(debitAccountId)) {
            log.warn("Transaction id: {}, debit account can't be same as credit account id", request.transactionId());
            throw new TransactionNotSupported("Debit account can't be same as credit account id");
        }

        Account debitAccount = accountCachedRepository.getAccount(request.debitAccountId());
        Account creditAccount = accountCachedRepository.getAccount(request.creditAccountId());
        if (debitAccount == null || creditAccount == null || !debitAccount.isActive() || !creditAccount.isActive()) {
            log.warn("Transaction id: {}, debit account can't be same as credit account id", request.transactionId());
            throw new TransactionNotSupported("Debit account or credit account id not exist or active");
        }
        if (debitAccount.getBalance().compareTo(request.amount()) < 0) {
            log.warn("Transaction id: {}, insufficient balance in debit account", request.transactionId());
            throw new TransactionNotSupported("Insufficient balance in debit account");
        }
        Transaction transaction = new Transaction();
        transaction.setId(request.transactionId());
        transaction.setAmount(request.amount());
        transaction.setDebitAccountId(debitAccount);
        transaction.setCreditAccountId(creditAccount);
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
            Account creditAccount = transaction.getCreditAccountId();
            debitAccount.setBalance(debitAccount.getBalance().subtract(transaction.getAmount()));
            creditAccount.setBalance(creditAccount.getBalance().add(transaction.getAmount()));
            accountCachedRepository.updateAccount(debitAccount);
            accountCachedRepository.updateAccount(creditAccount);
            transaction.setStatus(TransactionStatus.SUCCESS.getCode());
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED.getCode());
            transaction.setMessage("Transfer failed");
            log.error("Transfer failed for transaction {}", transaction.getId(), e);
            throw new TransactionExecutionException("Account Transfer failed");
        }
        transactionCachedRepository.updateTransaction(transaction);
    }
}
