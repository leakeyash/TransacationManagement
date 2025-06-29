package com.hsbc.transaction.model;

import lombok.Getter;

@Getter
public enum TransactionType {
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL"),
    TRANSFER("TRANSFER");

    private final String code;

    TransactionType(String code) {
        this.code = code;
    }

    public static TransactionType fromCode(String code) {
        for (TransactionType status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown transaction type code: " + code);
    }

    public static TransactionType fromName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown transaction type name: " + name);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
