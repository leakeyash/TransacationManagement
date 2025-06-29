package com.hsbc.transaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @Column(name = "transaction_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_account_id")
    private Account debitAccountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_account_id")
    private Account creditAccountId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status;

    @Column(length = 500)
    private String message;

    @Column(length = 255)
    private String description;

    @Column(name = "last_update_time")
    private LocalDateTime lastUpdateTime = LocalDateTime.now();

    @Column(name = "type")
    private String type;
}
