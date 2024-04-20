package ru.itmo.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Owner {
    @NonNull
    @EqualsAndHashCode.Include
    UUID id;
    @NonNull
    String name;
    @NonNull
    LocalDate birthday;
}
