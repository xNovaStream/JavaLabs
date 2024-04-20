package ru.itmo.dto.parser;

import lombok.NonNull;
import ru.itmo.dto.Owner;
import ru.itmo.entity.OwnerEntity;

public interface IOwnerParser {
    Owner toDto(@NonNull OwnerEntity ownerEntity);
    OwnerEntity toTransientEntity(@NonNull Owner owner);
}
