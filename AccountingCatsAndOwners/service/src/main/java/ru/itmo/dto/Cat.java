package ru.itmo.dto;

import lombok.*;
import ru.itmo.entity.CatColor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cat {
    @EqualsAndHashCode.Include
    UUID id;
    @NonNull
    String name;
    @NonNull
    LocalDate birthday;
    @NonNull
    String breed;
    @NonNull
    CatColor color;
    UUID ownerId;
}
