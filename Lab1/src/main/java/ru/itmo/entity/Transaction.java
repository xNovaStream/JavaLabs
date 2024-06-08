package ru.itmo.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Transaction implements Comparable<Transaction> {
    private final UUID id;
    private final UUID bankFromId;
    private final UUID accFromId;
    private final UUID bankToId;
    private final UUID accToId;
    private final double money;
    @Setter
    private boolean reversed = false;

    @Builder
    public Transaction(@NonNull UUID id,
                       UUID bankFromId,
                       UUID accFromId,
                       UUID bankToId,
                       UUID accToId,
                       double money) {
        this.id = id;
        this.bankFromId = bankFromId;
        this.accFromId = accFromId;
        this.bankToId = bankToId;
        this.accToId = accToId;
        this.money = money;
    }

    @Override
    public int compareTo(Transaction o) {
        return id.compareTo(o.id);
    }
}
