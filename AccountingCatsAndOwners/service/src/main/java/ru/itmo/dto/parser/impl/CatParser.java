package ru.itmo.dto.parser.impl;

import lombok.NonNull;
import ru.itmo.dto.Cat;
import ru.itmo.dto.parser.ICatParser;
import ru.itmo.entity.CatEntity;

public class CatParser implements ICatParser {
    @Override
    public Cat toDto(@NonNull CatEntity catEntity) {
        return Cat.builder()
                .id(catEntity.getId())
                .name(catEntity.getName())
                .birthday(catEntity.getBirthday())
                .breed(catEntity.getBreed())
                .color(catEntity.getColor())
                .ownerId(catEntity.getOwner() != null ? catEntity.getOwner().getId() : null)
                .build();
    }

    @Override
    public CatEntity toTransientEntity(@NonNull Cat cat) {
        return CatEntity.builder()
                .name(cat.getName())
                .birthday(cat.getBirthday())
                .breed(cat.getBreed())
                .color(cat.getColor())
                .build();
    }
}
