package com.hsbc.transaction.service;

import com.hsbc.transaction.cache.TransactionCachedRepository;
import com.hsbc.transaction.entity.Transaction;
import com.hsbc.transaction.exception.DuplicateTransactionException;
import com.hsbc.transaction.exception.TransactionNotFoundException;
import com.hsbc.transaction.handler.TransactionHandleProxy;
import com.hsbc.transaction.model.PageResponse;
import com.hsbc.transaction.model.TransactionRequest;
import com.hsbc.transaction.model.TransactionResponse;
import com.hsbc.transaction.model.TransactionStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
  private final TransactionCachedRepository transactionCachedRepository;
  private final TransactionHandleProxy transactionHandleProxy;

  @Autowired
  public TransactionServiceImpl(
      TransactionCachedRepository transactionCachedRepository,
      TransactionHandleProxy transactionHandleProxy) {
    this.transactionCachedRepository = transactionCachedRepository;
    this.transactionHandleProxy = transactionHandleProxy;
  }

  @Override
  @Transactional
  public TransactionResponse create(TransactionRequest request) {
    Transaction existingTransaction =
        transactionCachedRepository.getTransaction(request.transactionId());
    if (existingTransaction != null) {
      log.warn("Transaction already exists with id {}", request.transactionId());
      throw new DuplicateTransactionException("Transaction already exists");
    }
    return transactionHandleProxy.handleTransactionRequest(request);
  }

  @Override
  public TransactionResponse update(String id, TransactionRequest request) {
    Transaction transaction = transactionCachedRepository.getTransaction(id);
    if (transaction == null) {
      log.warn("Transaction id {} not found", id);
      throw new TransactionNotFoundException("Transaction not found");
    }
    if (!transaction.getStatus().equals(TransactionStatus.FAILED.getCode())) {
      log.warn("Transaction id {} is under processing or complete", id);
      throw new TransactionNotFoundException("Transaction is under processing or complete");
    }
    return transactionHandleProxy.handleTransactionRequest(request);
  }

  @Override
  public void delete(String id) {
    Transaction transaction = transactionCachedRepository.getTransaction(id);
    if (transaction == null) {
      throw new TransactionNotFoundException("Transaction not found: " + id);
    }
    transactionCachedRepository.deleteTransaction(transaction);
  }

  @Override
  public TransactionResponse getById(String id) {
    Transaction transaction = transactionCachedRepository.getTransaction(id);
    if (transaction == null) {
      throw new TransactionNotFoundException("Transaction not found: " + id);
    }
    return TransactionResponse.fromTransaction(transaction);
  }

  @Override
  public PageResponse<TransactionResponse> getAllPaged(Pageable pageable) {
    Page<Transaction> transactions = transactionCachedRepository.getTransactions(pageable);
    Page<TransactionResponse> transactionResponsePage =
        transactions.map(TransactionResponse::fromTransaction);
    return new PageResponse<>(transactionResponsePage);
  }
}
