package com.hsbc.transaction.exception;

public class TransactionExecutionException extends RuntimeException {
  public TransactionExecutionException(String message) {
    super(message);
  }
}
