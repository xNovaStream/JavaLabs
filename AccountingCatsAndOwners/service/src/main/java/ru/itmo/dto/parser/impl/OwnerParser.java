package ru.itmo.dto.parser.impl;

import lombok.NonNull;
import ru.itmo.dto.Owner;
import ru.itmo.dto.parser.IOwnerParser;
import ru.itmo.entity.OwnerEntity;

public class OwnerParser implements IOwnerParser {
    @Override
    public Owner toDto(@NonNull OwnerEntity ownerEntity) {
        return Owner.builder()
                .id(ownerEntity.getId())
                .name(ownerEntity.getName())
                .birthday(ownerEntity.getBirthday())
                .build();
    }

    @Override
    public OwnerEntity toTransientEntity(@NonNull Owner owner) {
        return OwnerEntity.builder()
                .name(owner.getName())
                .birthday(owner.getBirthday())
                .build();
    }
}
