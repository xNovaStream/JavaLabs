package ru.itmo.exception;

public class CentralBankException extends Exception {
    public CentralBankException(String message) {
        super(message);
    }
    public static CentralBankException BankAlreadyExists(String bankName) {
        return new CentralBankException(String.format("Bank with name %s already exists", bankName));
    }
}
