package com.hsbc.transaction.model;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    PROCESSING("PROCESSING", "Transaction is being processed"),
    SUCCESS("SUCCESS", "Transaction completed successfully"),
    FAILED("FAILED", "Transaction failed");

    private final String code;
    private final String description;

    TransactionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static TransactionStatus fromCode(String code) {
        for (TransactionStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown transaction status code: " + code);
    }

    public static TransactionStatus fromName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown transaction status name: " + name);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}