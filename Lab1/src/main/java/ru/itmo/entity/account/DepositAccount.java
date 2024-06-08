package ru.itmo.entity.account;

import ru.itmo.entity.Bank;
import ru.itmo.entity.IClock;
import ru.itmo.exception.AccountException;

import java.util.UUID;

public class DepositAccount extends AbstractBankAccount  {
    private boolean withdrawLocked;
    private double monthlyCashback;
    private final double startCapital;
    private int daysToUnlock;

    @Override
    public double changeMoneyImplDependentChecks(double delta) throws AccountException {
        if (delta < 0)
        {
            if (withdrawLocked) throw AccountException.AccountLockedException(id, delta, daysToUnlock);
            if (money + delta < 0) throw AccountException.NotEnoughMoneyException(id, money, delta, 0);
        }
        return delta;
    }

    public DepositAccount(UUID id, Bank bankOwner, boolean isDubious, double money, int daysToUnlock) throws AccountException {
        super(id, bankOwner, isDubious, money);
        this.withdrawLocked = true;
        this.startCapital = money;
        if (daysToUnlock < 0) throw new IllegalArgumentException("Days to unlock mustn't be negative");
        this.daysToUnlock = daysToUnlock;
    }

    @Override
    public void newDayEvent() {
        monthlyCashback += money * bankOwner.getDepositTable().calculateDailyPercent(startCapital);
        if (withdrawLocked) {
            --daysToUnlock;
            if (daysToUnlock == 0)
                withdrawLocked = false;
        }
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

    public static DepositAccountBuilder builder() {
        return new DepositAccountBuilder();
    }
    public static class DepositAccountBuilder extends AbstractBankAccountBuilder {
        private int daysToUnlock;
        public DepositAccountBuilder daysToUnlock(int daysToUnlock) {
            this.daysToUnlock = daysToUnlock;
            return this;
        }
        @Override
        public DepositAccount build() throws AccountException {
            return new DepositAccount(id, bankOwner, isDubious, money, daysToUnlock);
        }
    }
}
