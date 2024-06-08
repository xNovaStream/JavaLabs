package ru.itmo;

import ru.itmo.entity.Person;

public class PersonTest {
    public static Person.PersonBuilder personFullBuilder = Person.builder()
            .address("asa").passport("13423").firstName("Andrew").lastName("Glazkov");
    public static Person.PersonBuilder personNotFullBuilder = Person.builder()
            .firstName("Egor").lastName("Chuchman");
}
