package ru.itmo.dto.parser.impl;

import ru.itmo.dto.Owner;
import ru.itmo.dto.parser.IOwnerParser;
import ru.itmo.entity.OwnerEntity;

public class OwnerParser implements IOwnerParser {
    @Override
    public Owner toDto(OwnerEntity ownerEntity) {
        if (ownerEntity == null) return null;
        return Owner.builder()
                .id(ownerEntity.getId())
                .name(ownerEntity.getName())
                .birthday(ownerEntity.getBirthday())
                .build();
    }

    @Override
    public OwnerEntity toTransientEntity(Owner owner) {
        if (owner == null) return null;
        return OwnerEntity.builder()
                .name(owner.getName())
                .birthday(owner.getBirthday())
                .build();
    }
}
