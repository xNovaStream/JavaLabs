package ru.itmo.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import ru.itmo.entity.CatEntity;

import java.time.LocalDate;
import java.util.Set;
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
