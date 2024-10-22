package ru.itmo.service;

import lombok.NonNull;
import ru.itmo.dto.Cat;

import java.util.List;
import java.util.UUID;

public interface ICatService {
    Cat get(@NonNull UUID id);
    List<Cat> getAll();
    void add(@NonNull Cat cat);
    void delete(@NonNull UUID id);
    List<UUID> getFriendIds(@NonNull UUID catId);
    void makeFriends(@NonNull UUID catId1, @NonNull UUID catId2);
    void unmakeFriends(@NonNull UUID catId1, @NonNull UUID catId2);
}
