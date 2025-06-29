package com.hsbc.transaction.exception;

public class TransactionNotSupported extends RuntimeException {
  public TransactionNotSupported(String message) {
    super(message);
  }
}
