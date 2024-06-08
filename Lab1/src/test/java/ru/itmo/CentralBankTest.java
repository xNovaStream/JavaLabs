package ru.itmo;

import org.junit.jupiter.api.Test;
import ru.itmo.entity.CentralBank;
import ru.itmo.entity.Clock;
import ru.itmo.entity.ICentralBank;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CentralBankTest {
    @Test
    void registerBank() {
        ICentralBank centralBank = new CentralBank(new Clock());
        assertDoesNotThrow(() -> centralBank.registerBank(BankTest.tinkoffBuilder));
    }
}
