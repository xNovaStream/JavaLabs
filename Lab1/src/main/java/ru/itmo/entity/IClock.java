package ru.itmo.entity;

import ru.itmo.entity.account.IDateEventsSubscriber;

public interface IClock {
    void subscribe(IDateEventsSubscriber subscriber);
    void unsubscribe(IDateEventsSubscriber subscriber);
    void startClock(int days);
    int getDays();
}
