package ru.itmo.dto.parser;

import lombok.NonNull;
import ru.itmo.dto.Cat;
import ru.itmo.entity.CatEntity;

public interface ICatParser {
    Cat toDto(@NonNull CatEntity catEntity);
    CatEntity toTransientEntity(@NonNull Cat cat);
}
