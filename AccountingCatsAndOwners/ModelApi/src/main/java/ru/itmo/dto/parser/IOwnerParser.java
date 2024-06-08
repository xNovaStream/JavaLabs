package ru.itmo.dto.parser;

import ru.itmo.dto.Owner;
import ru.itmo.entity.OwnerEntity;

public interface IOwnerParser {
    Owner toDto(OwnerEntity ownerEntity);
    OwnerEntity toTransientEntity(Owner owner);
}
