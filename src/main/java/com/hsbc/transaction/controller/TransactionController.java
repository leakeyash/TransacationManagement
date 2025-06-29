package com.hsbc.transaction.controller;

import com.hsbc.transaction.model.PageResponse;
import com.hsbc.transaction.model.TransactionRequest;
import com.hsbc.transaction.model.TransactionResponse;
import com.hsbc.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Management", description = "Transaction Management APIs")
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody TransactionRequest request) {
        TransactionResponse transactionResponse = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/transactions/" + transactionResponse.transactionId()))  // 返回201 Created
                .body(transactionResponse);
    }

    @PutMapping("/{id}")
    public TransactionResponse update(@PathVariable String id, @RequestBody TransactionRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public TransactionResponse getById(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    public PageResponse<TransactionResponse> getAllPaged(@RequestParam int page, @RequestParam int size) {
        return service.getAllPaged(PageRequest.of(page, size));
    }
}
