package ru.itmo.exception;

import java.util.UUID;

public class AccountException extends Exception {
    public AccountException(String message) {
        super(message);
    }
    public static AccountException NotEnoughMoneyException(UUID accId, double balance, double tryingTake, double creditLimit) {
        return new AccountException(String.format("Can't change balance on %.2f from account %s, balance is %.2f, limit is %.2f", tryingTake, accId, balance, creditLimit));
    }
    public static AccountException AccountLockedException(UUID id, double tryingTake, int daysToUnlock)
    {
        return new AccountException(String.format("Can't withdraw %.2f from account %s. It is locked until %d", tryingTake, id, daysToUnlock));
    }

    public static AccountException DubiousLimitException(UUID id, double tryingTake, double dubiousLimit)
    {
        return new AccountException(String.format("Can't withdraw %.2f from account %s. It is dubious and has limit %.2f on withdraw", tryingTake, id, dubiousLimit));
    }
}
