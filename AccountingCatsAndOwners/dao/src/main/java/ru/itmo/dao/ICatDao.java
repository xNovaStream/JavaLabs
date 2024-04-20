package ru.itmo.dao;

import lombok.NonNull;
import ru.itmo.entity.CatEntity;

import java.util.List;
import java.util.UUID;

public interface ICatDao {
    CatEntity findById(@NonNull UUID id);
    List<CatEntity> findAll();
    void save(@NonNull CatEntity cat);
    void update(@NonNull CatEntity cat);
    void updateAll(@NonNull CatEntity... cats);
    void delete(@NonNull UUID id);
    List<CatEntity> getFriendIds(@NonNull UUID id);
}
