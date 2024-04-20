package ru.itmo.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import ru.itmo.entity.CatColor;

import java.time.LocalDate;
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
