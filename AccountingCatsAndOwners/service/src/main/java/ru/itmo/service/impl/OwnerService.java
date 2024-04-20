package ru.itmo.service.impl;

import lombok.NonNull;
import ru.itmo.dao.ICatDao;
import ru.itmo.dao.IOwnerDao;
import ru.itmo.dto.Owner;
import ru.itmo.dto.parser.IOwnerParser;
import ru.itmo.entity.CatEntity;
import ru.itmo.entity.OwnerEntity;
import ru.itmo.service.IOwnerService;

import java.util.List;
import java.util.UUID;

public class OwnerService implements IOwnerService {
    private final IOwnerDao ownerDao;
    private final ICatDao catDao;
    private final IOwnerParser ownerParser;

    public OwnerService(@NonNull IOwnerDao ownerDao,
                        @NonNull ICatDao catDao,
                        @NonNull IOwnerParser ownerParser) {
        this.ownerDao = ownerDao;
        this.catDao = catDao;
        this.ownerParser = ownerParser;
    }

    @Override
    public Owner get(@NonNull UUID id) {
        return ownerParser.toDto(ownerDao.getById(id));
    }

    @Override
    public List<Owner> getAll() {
        return ownerDao.getAll().stream().map(ownerParser::toDto).toList();
    }

    @Override
    public void add(@NonNull Owner owner) {
        ownerDao.save(ownerParser.toTransientEntity(owner));
    }

    @Override
    public void delete(@NonNull UUID id) {
        ownerDao.delete(id);
    }

    @Override
    public List<UUID> getCats(@NonNull UUID id) {
        List<CatEntity> cats = ownerDao.getCats(id);
        return cats != null ? cats.stream().map(CatEntity::getId).toList() : null;
    }

    @Override
    public void takeCat(@NonNull UUID ownerId, @NonNull UUID catId) {
        OwnerEntity owner = ownerDao.getById(ownerId);
        CatEntity cat = catDao.findById(catId);
        if (owner != null && cat != null && cat.getOwner() == null) {
            owner.getCats().add(cat);
            ownerDao.update(owner);
        }
    }

    @Override
    public void giveCat(@NonNull UUID ownerId, @NonNull UUID catId) {
        OwnerEntity owner = ownerDao.getById(ownerId);
        CatEntity cat = catDao.findById(catId);
        if (owner != null && cat != null && cat.getOwner() == owner) {
            owner.getCats().remove(cat);
            ownerDao.update(owner);
        }
    }

    @Override
    public void giveCat(@NonNull UUID oldOwnerId, @NonNull UUID newOwnerId, @NonNull UUID catId) {
        OwnerEntity oldOwner = ownerDao.getById(oldOwnerId);
        OwnerEntity newOwner = ownerDao.getById(newOwnerId);
        CatEntity cat = catDao.findById(catId);
        if (oldOwner != null && newOwner != null && cat != null && cat.getOwner() == oldOwner) {
            newOwner.getCats().add(cat);
            ownerDao.update(newOwner);
        }
    }
}
