package ru.itmo.entity;

import ru.itmo.exception.AccountException;
import ru.itmo.exception.CentralBankException;

import java.util.Set;
import java.util.UUID;

public interface ICentralBank {
    IClock getClock();
    Set<Bank> getBanks();
    Bank getBank(UUID id);
    Set<Transaction> getTransactions();
    Bank registerBank(Bank.BankBuilder builder) throws CentralBankException;
    void crossBankPayment(UUID bankIdFrom, UUID accIdFrom, UUID bankIdTo, UUID accIdTo, double money, Transaction.TransactionBuilder builder) throws AccountException;
}
