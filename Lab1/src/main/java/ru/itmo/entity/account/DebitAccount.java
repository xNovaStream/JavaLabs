package ru.itmo.entity.account;

import lombok.Builder;
import ru.itmo.entity.Bank;
import ru.itmo.entity.IClock;
import ru.itmo.exception.AccountException;

import java.util.UUID;

public class DebitAccount extends AbstractBankAccount {
    private double monthlyCashback;

    @Override
    public double changeMoneyImplDependentChecks(double delta) throws AccountException {
        if (money + delta < 0)
            throw AccountException.NotEnoughMoneyException(id, money, delta, 0);
        return delta;
    }

    public DebitAccount(UUID id, Bank bankOwner, boolean isDubious, double money) throws AccountException {
        super(id, bankOwner, isDubious, money);
    }

    @Override
    public void newDayEvent() {
        monthlyCashback += bankOwner.getDailyPercentForDebitAcc() * money;
    }
    @Override
    public void endOfMonthEvent() {
        money += monthlyCashback;
        monthlyCashback = 0;
    }
    @Override
    public void subscribeMe(IClock clock) {
        clock.subscribe(this);
    }

    @Override
    public void unsubscribeMe(IClock clock) {
        clock.unsubscribe(this);
    }

    public static DebitAccountBuilder builder() {
        return new DebitAccountBuilder();
    }
    public static class DebitAccountBuilder extends AbstractBankAccountBuilder {
        @Override
        public DebitAccount build() throws AccountException {
            return new DebitAccount(id, bankOwner, isDubious, money);
        }
    }
}
