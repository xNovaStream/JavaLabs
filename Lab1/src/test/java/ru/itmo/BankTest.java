package ru.itmo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.itmo.entity.*;
import ru.itmo.entity.account.AbstractBankAccount;
import ru.itmo.exception.AccountException;
import ru.itmo.exception.CentralBankException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {
    public static final Bank.BankBuilder sberBankBuilder = Bank.builder().name("SberBank")
            .depositTable(DepositTableTest.depositTable1).dailyPercentForDebitAcc(10.0 / 365)
            .fixedFeeForCreditAcc(10).limitForDubiousAcc(50).negativeLimitForCreditAcc(-100);
    public static final Bank.BankBuilder tinkoffBuilder = Bank.builder().name("tinkoff")
            .depositTable(DepositTableTest.depositTable1).dailyPercentForDebitAcc(9.0 / 365)
            .fixedFeeForCreditAcc(5).limitForDubiousAcc(50).negativeLimitForCreditAcc(-100);
    public static ICentralBank centralBank;
    public static Bank sberBank;
    public static Bank tinkoff;
    public static Person fullPerson;
    public static Person notFullPerson;
    public static Person fullTinkPerson;
    @BeforeAll
    static void createTest() throws CentralBankException, AccountException {
        centralBank = new CentralBank(new Clock());
        sberBank = centralBank.registerBank(sberBankBuilder);
        tinkoff = centralBank.registerBank(tinkoffBuilder);
        UUID fullId = registerPerson(PersonTest.personFullBuilder, sberBank);
        UUID notFullId = registerPerson(PersonTest.personNotFullBuilder, sberBank);
        fullPerson = sberBank.getClients().stream().filter(p -> p.getId() == fullId).findFirst().orElseThrow();
        notFullPerson = sberBank.getClients().stream().filter(p -> p.getId() == notFullId).findFirst().orElseThrow();

        UUID fullInTinkoff = registerPerson(PersonTest.personFullBuilder, tinkoff);
        fullTinkPerson = tinkoff.getClients().iterator().next();
        UUID id1, id2, id3;
        id1 = sberBank.registerAccount(fullPerson, AccountTest.CreditAccountBuilder);
        id2 = sberBank.registerAccount(fullPerson, AccountTest.DepositAccountBuilder);
        id3 = sberBank.registerAccount(notFullPerson, AccountTest.DebitAccountBuilder);
        credAcc1 = sberBank.getAccounts().stream().filter(acc -> acc.getId() == id1).findFirst().orElseThrow();
        deposAcc1 = sberBank.getAccounts().stream().filter(acc -> acc.getId() == id2).findFirst().orElseThrow();
        debitAcc1 = sberBank.getAccounts().stream().filter(acc -> acc.getId() == id3).findFirst().orElseThrow();
        tinkoff.registerAccount(fullTinkPerson, AccountTest.DebitAccountBuilder);
        debitAccTink1 = tinkoff.getAccounts().iterator().next();
    }

    public static UUID registerPerson(Person.PersonBuilder builder, Bank bank) {
        return bank.registerPerson(builder);
    }

    @Test
    void registerPersonTest() {
        assertEquals(2, sberBank.getClients().size());

    }
    private static AbstractBankAccount credAcc1;
    private static AbstractBankAccount deposAcc1;
    private static AbstractBankAccount debitAcc1;
    private static AbstractBankAccount debitAccTink1;

    @Test
    void withdrawFromAccount() {
        assertDoesNotThrow(() -> sberBank.withdrawFromAccount(credAcc1.getId(), 120));
        assertThrows(AccountException.class, () -> sberBank.withdrawFromAccount(credAcc1.getId(), 120));
        assertThrows(AccountException.class, () -> sberBank.withdrawFromAccount(deposAcc1.getId(), 1));
        assertThrows(AccountException.class, () -> sberBank.withdrawFromAccount(debitAcc1.getId(), 102));
    }

    @Test
    void transferToAnotherBank() {
        UUID id = assertDoesNotThrow(() -> sberBank.transferToAnotherBank(debitAcc1.getId(), tinkoff.getId(), debitAccTink1.getId(), 50));
        assertEquals(101 - 50, debitAcc1.getMoney());
        assertEquals(101 + 50, debitAccTink1.getMoney());
        Transaction transaction = sberBank.getTransactions().stream().filter(t -> t.getId() == id).findFirst().orElseThrow();
        assertDoesNotThrow(() -> sberBank.cancelTransaction(transaction));
        assertEquals(101, debitAcc1.getMoney());
        assertEquals(101, debitAccTink1.getMoney());
    }
}
