package ru.itmo.entity;

import lombok.*;
import ru.itmo.entity.account.AbstractBankAccount;

import java.util.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person implements Comparable<Person> {
    @EqualsAndHashCode.Include
    private final UUID id;
    private String firstName;
    private String lastName;
    private String address;
    private String passport;
    private final Set<AbstractBankAccount> accounts = new TreeSet<>();
    @Builder
    public Person(@NonNull UUID id, @NonNull String firstName, @NonNull String lastName, String address, String passport)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.passport = passport;
    }

    public void addAccount(AbstractBankAccount account) {
        accounts.add(account);
    }

    public void removeAccount(AbstractBankAccount account) {
        accounts.remove(account);
    }

    @Override
    public int compareTo(Person person) {
        return id.compareTo(person.id);
    }
}
