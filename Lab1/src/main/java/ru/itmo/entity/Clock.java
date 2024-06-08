package ru.itmo.entity;

import ru.itmo.entity.account.IDateEventsSubscriber;

import java.util.ArrayList;

public class Clock implements IClock {
    private int currentDay = 1;
    private final ArrayList<IDateEventsSubscriber> subscribers = new ArrayList<>();
    @Override
    public void subscribe(IDateEventsSubscriber subscriber) {
        if (!subscribers.contains(subscriber))
            subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(IDateEventsSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void startClock(int days) {
        for (int i = 0; i < days; i++) {
            for (var subscriber : subscribers)
                subscriber.newDayEvent();

            ++currentDay;
            if (currentDay == 31) {
                for (var subscriber : subscribers)
                    subscriber.endOfMonthEvent();
                currentDay = 1;
            }
        }
    }

    @Override
    public int getDays() {
        return currentDay;
    }
}
