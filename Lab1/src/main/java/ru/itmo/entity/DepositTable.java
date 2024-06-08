package ru.itmo.entity;

import java.util.List;

public class DepositTable {
    private final List<Double> startCapitals;
    private final List<Double> dailyPercents;

    public DepositTable(List<Double> startCapitals, List<Double> dailyPercents)
    {
        if (dailyPercents.size() - startCapitals.size() != 1)
            throw new IllegalArgumentException("Incorrect length of parameters");

        if (!startCapitals.isEmpty()) {
            if (startCapitals.get(0) <= 0)
                throw new IllegalArgumentException("First elem of start capital must be positive");

            for (int i = 1; i < startCapitals.size(); i++) {
                if (startCapitals.get(i) <= startCapitals.get(i - 1))
                    throw new IllegalArgumentException("Start capitals must grow");
            }
        }

        this.startCapitals = startCapitals;
        this.dailyPercents = dailyPercents;
    }

    public double calculateDailyPercent(double money)
    {
        int i = 0;
        for (; i < startCapitals.size(); i++) {
            if (money < startCapitals.get(i)) break;
        }
        return dailyPercents.get(i);
    }
}
