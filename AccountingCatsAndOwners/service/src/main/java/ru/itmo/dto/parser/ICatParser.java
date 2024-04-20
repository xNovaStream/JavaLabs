package ru.itmo.dto.parser;

import ru.itmo.dto.Cat;
import ru.itmo.entity.CatEntity;

public interface ICatParser {
    Cat toDto(CatEntity catEntity);
    CatEntity toTransientEntity(Cat cat);
}
