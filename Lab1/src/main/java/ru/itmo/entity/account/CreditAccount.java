package ru.itmo.entity.account;

import ru.itmo.entity.Bank;
import ru.itmo.entity.IClock;
import ru.itmo.exception.AccountException;

import java.util.UUID;

public class CreditAccount extends AbstractBankAccount {
    @Override
    protected double changeMoneyImplDependentChecks(double delta) throws AccountException {
        if (money < 0)
            delta -= bankOwner.getFixedFeeForCreditAcc();

        if (money + delta < bankOwner.getNegativeLimitForCreditAcc())
            throw AccountException.NotEnoughMoneyException(id, money, delta, bankOwner.getNegativeLimitForCreditAcc());
        return delta;
    }

    public CreditAccount(UUID id, Bank bankOwner, boolean isDubious, double money) throws AccountException {
        super(id, bankOwner, isDubious, money);
    }

    @Override
    public void newDayEvent() {}
    @Override
    public void endOfMonthEvent() {}
    @Override
    public void subscribeMe(IClock clock) {}
    @Override
    public void unsubscribeMe(IClock clock) {}

    public static CreditAccountBuilder builder() {
        return new CreditAccountBuilder();
    }
    public static class CreditAccountBuilder extends AbstractBankAccountBuilder {
        @Override
        public CreditAccount build() throws AccountException {
            return new CreditAccount(id, bankOwner, isDubious, money);
        }
    }
}
