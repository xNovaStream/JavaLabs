package ru.itmo;

import org.junit.jupiter.api.Test;
import ru.itmo.entity.DepositTable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositTableTest {
    public static DepositTable depositTable1 = new DepositTable(List.of(100.0, 200.0), List.of(5.0 / 365, 20.0 / 365, 30.0 / 365));
    public static DepositTable depositTable2 = new DepositTable(List.of(), List.of(5.0));

    @Test
    void testWrongTables() {
        assertThrows(IllegalArgumentException.class, () -> new DepositTable(List.of(200.0, 100.0), List.of(5.0, 6.0, 7.0)));
        assertThrows(IllegalArgumentException.class, () -> new DepositTable(List.of(-100.0, 500.0), List.of(5.0, 6.0, 7.0)));
        assertThrows(IllegalArgumentException.class, () -> new DepositTable(List.of(200.0, -100.0), List.of(5.0, 6.0, 7.0)));
        assertThrows(IllegalArgumentException.class, () -> new DepositTable(List.of(200.0), List.of(5.0, 6.0, 7.0)));
        assertThrows(IllegalArgumentException.class, () -> new DepositTable(List.of(200.0, 100.0), List.of(5.0)));
        assertThrows(IllegalArgumentException.class, () -> new DepositTable(List.of(200.0, 100.0), List.of(5.0, 6.0, 7.0)));
    }

    @Test
    void testGoodTables() {
        assertDoesNotThrow(() -> new DepositTable(List.of(), List.of(5.0)));
        assertDoesNotThrow(() -> new DepositTable(List.of(100.0, 200.0), List.of(5.0 / 365, 20.0 / 365, 30.0 / 365)));
    }
    @Test
    void calculateDailyPercent() {
        assertEquals(depositTable1.calculateDailyPercent(150), 20.0 / 365);
        assertEquals(depositTable1.calculateDailyPercent(50), 5.0 / 365);
        assertEquals(depositTable1.calculateDailyPercent(230), 30.0 / 365);
        assertEquals(depositTable2.calculateDailyPercent(100), 5);
        assertEquals(depositTable2.calculateDailyPercent(1), 5);
    }
}
