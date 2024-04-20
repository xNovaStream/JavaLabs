package ru.itmo.dao;

import lombok.NonNull;
import ru.itmo.entity.CatEntity;
import ru.itmo.entity.OwnerEntity;

import java.util.List;
import java.util.UUID;

public interface IOwnerDao {
    OwnerEntity getById(@NonNull UUID id);
    List<OwnerEntity> getAll();
    void save(@NonNull OwnerEntity owner);
    void update(@NonNull OwnerEntity cat);
    void updateAll(@NonNull OwnerEntity... cats);
    void delete(@NonNull UUID id);
    List<CatEntity> getCats(@NonNull UUID id);
}
