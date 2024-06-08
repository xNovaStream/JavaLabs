package ru.itmo.entity;

import lombok.*;
import ru.itmo.entity.account.AbstractBankAccount;
import ru.itmo.exception.AccountException;
import ru.itmo.exception.TransactionException;

import java.util.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class Bank implements Comparable<Bank> {
    @EqualsAndHashCode.Include
    private final UUID id;
    @Setter
    private String name;
    private final ICentralBank centralBank;
    private double dailyPercentForDebitAcc;
    private double fixedFeeForCreditAcc;
    private double negativeLimitForCreditAcc;
    @Setter
    private DepositTable depositTable;
    private double limitForDubiousAcc;
    private final Set<AbstractBankAccount> accounts = new TreeSet<>();
    private final Set<Person> clients = new TreeSet<>();
    private final Set<Transaction> transactions = new TreeSet<>();

    @Builder
    public Bank(@NonNull UUID id,
                @NonNull String name,
                @NonNull ICentralBank centralBank,
                double dailyPercentForDebitAcc,
                double fixedFeeForCreditAcc,
                double negativeLimitForCreditAcc,
                @NonNull DepositTable depositTable,
                double limitForDubiousAcc) {
        this.id = id;
        this.name = name;
        this.centralBank = centralBank;
        setDailyPercentForDebitAcc(dailyPercentForDebitAcc);
        setFixedFeeForCreditAcc(fixedFeeForCreditAcc);
        setNegativeLimitForCreditAcc(negativeLimitForCreditAcc);
        this.depositTable = depositTable;
        setLimitForDubiousAcc(limitForDubiousAcc);
    }

    public void setDailyPercentForDebitAcc(double dailyPercentForDebitAcc) {
        if (dailyPercentForDebitAcc < 0)
            throw new IllegalArgumentException("Negative daily percent for debit account");
        this.dailyPercentForDebitAcc = dailyPercentForDebitAcc;
    }

    public void setFixedFeeForCreditAcc(double fixedFeeForCreditAcc) {
        if (fixedFeeForCreditAcc < 0)
            throw new IllegalArgumentException("Negative fixed fee for credit account");
        this.fixedFeeForCreditAcc = fixedFeeForCreditAcc;
    }

    public void setNegativeLimitForCreditAcc(double negativeLimitForCreditAcc) {
        if (negativeLimitForCreditAcc > 0)
            throw new IllegalArgumentException("Negative limit for credit account must be negative");
        this.negativeLimitForCreditAcc = negativeLimitForCreditAcc;
    }

    public void setLimitForDubiousAcc(double limitForDubiousAcc) {
        if (limitForDubiousAcc < 0)
            throw new IllegalArgumentException("Limit for dubious account must be positive");
        this.limitForDubiousAcc = limitForDubiousAcc;
    }

    public UUID registerPerson(Person.PersonBuilder personBuilder) {
        Person newPerson = personBuilder.id(UUID.randomUUID()).build();
        clients.add(newPerson);
        return newPerson.getId();
    }

    public UUID registerAccount(Person person, AbstractBankAccount.AbstractBankAccountBuilder accountBuilder) throws AccountException {
        if (person.getAddress() == null || person.getPassport() == null)
            accountBuilder.isDubious(true);
        AbstractBankAccount newAcc = accountBuilder.bankOwner(this).id(UUID.randomUUID()).build();
        person.addAccount(newAcc);
        newAcc.subscribeMe(centralBank.getClock());
        accounts.add(newAcc);
        return newAcc.getId();
    }

    public void removeAccount(Person person, AbstractBankAccount account) {
        person.removeAccount(account);
        accounts.remove(account);
        account.unsubscribeMe(centralBank.getClock());
    }

    public void removePerson(Person person) {
        person.getAccounts().forEach(accounts::remove);
        clients.remove(person);
    }

    private AbstractBankAccount getAccount(UUID id) {
        return accounts.stream().filter(acc -> acc.getId() == id).findFirst().orElse(null);
    }

    private void changeMoneyOnAcc(UUID accId, double money, Transaction.TransactionBuilder builder) throws AccountException {
        AbstractBankAccount account = getAccount(accId);
        if (account == null) throw new IllegalArgumentException("Invalid account id");

        account.changeMoney(money);

        if (money >= 0)
            builder.bankToId(this.id).accToId(accId).money(money);
        else
            builder.bankFromId(this.id).accFromId(accId).money(-money);
    }

    public void withdrawFromAccount(UUID accId, double money) throws AccountException {
        if (money < 0) throw new IllegalArgumentException("Money mustn't be negative");
        Transaction.TransactionBuilder builder = Transaction.builder().id(UUID.randomUUID());
        changeMoneyOnAcc(accId, -money, builder);
        transactions.add(builder.build());
    }

    public void topUpAccount(UUID accId, double money) throws AccountException {
        if (money < 0) throw new IllegalArgumentException("Money mustn't be negative");
        Transaction.TransactionBuilder builder = Transaction.builder().id(UUID.randomUUID());
        changeMoneyOnAcc(accId, money, builder);
        transactions.add(builder.build());
    }

    public void transferMoney(UUID accIdFrom, UUID accIdTo, double money) throws AccountException {
        if (money < 0) throw new IllegalArgumentException("Money mustn't be negative");
        Transaction.TransactionBuilder builder = Transaction.builder().id(UUID.randomUUID());
        changeMoneyOnAcc(accIdFrom, -money, builder);
        try {
            changeMoneyOnAcc(accIdTo, money, builder);
        }
        catch (Exception e) {
            changeMoneyOnAcc(accIdFrom, money, builder);
            throw e;
        }
        transactions.add(builder.build());
    }

    public UUID transferToAnotherBank(UUID accIdFrom, UUID otherBankId, UUID accIdTo, double money) throws AccountException {
        if (money < 0) throw new IllegalArgumentException("Money mustn't be negative");
        UUID newTransactionId = UUID.randomUUID();
        Transaction.TransactionBuilder builder = Transaction.builder().id(newTransactionId);
        changeMoneyOnAcc(accIdFrom, -money, builder);
        try {
            centralBank.crossBankPayment(this.id, accIdFrom, otherBankId, accIdTo, money, builder);
        }
        catch (Exception e) {
            changeMoneyOnAcc(accIdFrom, money, builder);
            throw e;
        }
        transactions.add(builder.build());
        return newTransactionId;
    }

    public void acceptFromAnotherBank(UUID accIdTo, double money, Transaction.TransactionBuilder builder) throws AccountException {
        if (money < 0) throw  new IllegalArgumentException("Money mustn't be negative");
        changeMoneyOnAcc(accIdTo, money, builder);
        transactions.add(builder.build());
    }

    public void cancelTransaction(Transaction transaction) throws TransactionException, AccountException {
        if (id != transaction.getBankFromId()) throw TransactionException.CancelDenied(id, transaction.getId());
        if (transaction.isReversed()) throw TransactionException.AlreadyReversed(id);
        if (transaction.getBankToId() != null) {
            centralBank.getBank(transaction.getBankToId()).transferToAnotherBank(transaction.getAccToId(), transaction.getBankFromId(), transaction.getAccFromId(), transaction.getMoney());
        }
        else if (transaction.getAccToId() != null) {
            transferMoney(transaction.getAccToId(), transaction.getAccFromId(), transaction.getMoney());
        }
        else {
            topUpAccount(transaction.getAccFromId(), transaction.getMoney());
        }
        transaction.setReversed(true);
    }

    @Override
    public int compareTo(Bank bank) {
        return name.compareTo(bank.name);
    }
}
