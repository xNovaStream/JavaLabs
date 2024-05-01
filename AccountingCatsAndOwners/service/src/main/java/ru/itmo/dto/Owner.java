package ru.itmo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Owner {
    @EqualsAndHashCode.Include
    UUID id;
    @NonNull
    @NotBlank(message = "Name mustn't be empty")
    String name;
    @NonNull
    LocalDate birthday;
}
