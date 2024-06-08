package ru.itmo.entity.account;

import ru.itmo.entity.Bank;
import ru.itmo.exception.AccountException;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
public abstract class AbstractBankAccount implements Comparable<AbstractBankAccount>, IDateEventsSubscriber {
    @Getter @Setter protected boolean isDubious;
    protected final Bank bankOwner;
    @Getter protected final UUID id;
    @Getter protected double money;
    protected abstract double changeMoneyImplDependentChecks(double delta) throws AccountException;

    protected AbstractBankAccount(UUID id, Bank bankOwner, boolean isDubious, double money) throws AccountException {
        this.bankOwner = bankOwner;
        this.money = 0;
        this.isDubious = isDubious;
        this.id = id;
        changeMoney(money);
    }

    public void changeMoney(double delta) throws AccountException {
        delta = changeMoneyImplDependentChecks(delta);
        if (isDubious && delta < -bankOwner.getLimitForDubiousAcc())
            throw AccountException.DubiousLimitException(id, delta, bankOwner.getLimitForDubiousAcc());
        money += delta;
    }

    @Override
    public int compareTo(AbstractBankAccount o) {
        return id.compareTo(o.id);
    }

    public abstract static class AbstractBankAccountBuilder {
        protected boolean isDubious;
        protected Bank bankOwner;
        protected UUID id;
        protected double money;

        protected AbstractBankAccountBuilder() {
        }

        public AbstractBankAccountBuilder isDubious(boolean isDubious) {
            this.isDubious = isDubious;
            return this;
        }

        public AbstractBankAccountBuilder bankOwner(Bank bankOwner) {
            this.bankOwner = bankOwner;
            return this;
        }

        public AbstractBankAccountBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AbstractBankAccountBuilder money(double money) {
            this.money = money;
            return this;
        }

        public abstract AbstractBankAccount build() throws AccountException;
    }
}
