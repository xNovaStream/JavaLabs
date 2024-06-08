package ru.itmo;

import ru.itmo.entity.account.AbstractBankAccount;
import ru.itmo.entity.account.CreditAccount;
import ru.itmo.entity.account.DebitAccount;
import ru.itmo.entity.account.DepositAccount;

public class AccountTest {
    public static AbstractBankAccount.AbstractBankAccountBuilder CreditAccountBuilder = CreditAccount.builder()
            .money(101);
    public static DebitAccount.AbstractBankAccountBuilder DebitAccountBuilder = DebitAccount.builder()
            .money(101);
    public static DepositAccount.AbstractBankAccountBuilder DepositAccountBuilder = DepositAccount.builder()
            .daysToUnlock(10).money(101);
}
