package ru.itmo.entity;

import lombok.Getter;
import lombok.NonNull;
import ru.itmo.exception.AccountException;
import ru.itmo.exception.CentralBankException;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@Getter
public class CentralBank implements ICentralBank {
    private final IClock clock;
    private final Set<Bank> banks = new TreeSet<>();
    private final Set<Transaction> transactions = new TreeSet<>();

    public CentralBank(@NonNull IClock clock) {
        this.clock = clock;
    }

    @Override
    public Bank registerBank(Bank.BankBuilder builder) throws CentralBankException {
        Bank newBank = builder.id(UUID.randomUUID()).centralBank(this).build();
        if (!banks.add(newBank))
            throw CentralBankException.BankAlreadyExists(newBank.getName());
        return newBank;
    }

    @Override
    public Bank getBank(UUID id) {
        return banks.stream().filter(bank -> bank.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void crossBankPayment(UUID bankIdFrom, UUID accIdFrom, UUID bankIdTo, UUID accIdTo, double money, Transaction.TransactionBuilder builder) throws AccountException {
        Bank bankTo = getBank(bankIdTo);
        if (bankTo == null) throw new IllegalArgumentException("Bank not found");
        bankTo.acceptFromAnotherBank(accIdTo, money, builder);
    }
}
