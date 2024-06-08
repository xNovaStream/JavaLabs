package ru.itmo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.itmo.entity.UserRole;

import java.io.Serializable;
import java.util.UUID;


@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {
    @NotNull
    UUID ownerId;
    @NotBlank
    String login;
    @NotBlank
    String password;
    @NotNull
    UserRole role;
}
