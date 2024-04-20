package ru.itmo.service;

import lombok.NonNull;
import ru.itmo.dto.Owner;

import java.util.List;
import java.util.UUID;

public interface IOwnerService {
    Owner get(@NonNull UUID id);
    List<Owner> getAll();
    void add(@NonNull Owner owner);
    void delete(@NonNull UUID id);
    List<UUID> getCats(@NonNull UUID id);
    void takeCat(@NonNull UUID ownerId, @NonNull UUID catId);
    void giveCat(@NonNull UUID ownerId, @NonNull UUID catId);
    void giveCat(@NonNull UUID oldOwnerId, @NonNull UUID newOwnerId, @NonNull UUID catId);
}
