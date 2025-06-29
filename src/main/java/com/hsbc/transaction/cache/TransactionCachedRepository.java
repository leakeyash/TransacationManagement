package com.hsbc.transaction.cache;

import com.hsbc.transaction.entity.Transaction;
import com.hsbc.transaction.repository.TransactionRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = "transactionCache")
public class TransactionCachedRepository {
    private final TransactionRepository transactionRepository;

    public TransactionCachedRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Cacheable(key = "#transactionId")
    public Transaction getTransaction(String transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    @Cacheable(key = "#pageable")
    public Page<Transaction> getTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @CacheEvict(allEntries = true)
    @CachePut(key = "#transaction.id")
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @CacheEvict(allEntries = true)
    public void deleteTransaction(Transaction transaction) {
        transactionRepository.delete(transaction);
    }
}
