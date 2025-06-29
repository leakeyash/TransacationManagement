package com.hsbc.transaction.service;

import com.hsbc.transaction.model.PageResponse;
import com.hsbc.transaction.model.TransactionRequest;
import com.hsbc.transaction.model.TransactionResponse;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
    TransactionResponse update(String id, TransactionRequest request);
    void delete(String id);
    TransactionResponse getById(String id);
    PageResponse<TransactionResponse> getAllPaged(Pageable pageable);
}
