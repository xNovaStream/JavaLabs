package ru.itmo.entity.account;

import ru.itmo.entity.IClock;

public interface IDateEventsSubscriber {

    void newDayEvent();

    void endOfMonthEvent();

    void subscribeMe(IClock clock);

    void unsubscribeMe(IClock clock);
}
