package ru.itmo.dto.parser;

import ru.itmo.dto.User;
import ru.itmo.entity.UserEntity;

public interface IUserParser {
    User toDto(UserEntity userEntity);
    UserEntity toEntity(User user);
}
