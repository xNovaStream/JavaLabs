package ru.itmo.exception;


import java.util.UUID;

public class TransactionException extends Exception {
    public TransactionException(String message) {
        super(message);
    }
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public static TransactionException CancelDenied(UUID bankId, UUID transactionId) {
        return new TransactionException(String.format("Bank %s cant reverse transaction %s", bankId, transactionId));
    }
    public static TransactionException AlreadyReversed(UUID transactionId) {
        return new TransactionException(String.format("Transaction %s already reversed", transactionId));
    }
}
