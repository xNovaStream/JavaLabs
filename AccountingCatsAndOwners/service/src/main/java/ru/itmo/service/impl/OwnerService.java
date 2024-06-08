package ru.itmo.service.impl;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.dao.ICatDao;
import ru.itmo.dao.IOwnerDao;
import ru.itmo.dao.IUserDao;
import ru.itmo.dto.Owner;
import ru.itmo.dto.User;
import ru.itmo.dto.parser.IOwnerParser;
import ru.itmo.dto.parser.IUserParser;
import ru.itmo.entity.CatEntity;
import ru.itmo.entity.OwnerEntity;
import ru.itmo.entity.UserEntity;
import ru.itmo.service.IOwnerService;

import java.util.List;
import java.util.UUID;

@Service
public class OwnerService implements IOwnerService {
    private final IOwnerDao ownerDao;
    private final ICatDao catDao;
    private final IUserDao userDao;
    private final IOwnerParser ownerParser;
    private final IUserParser userParser;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OwnerService(@NonNull IOwnerDao ownerDao,
                        @NonNull ICatDao catDao,
                        @NonNull IUserDao userDao,
                        @NonNull IOwnerParser ownerParser,
                        @NonNull IUserParser userParser,
                        @NonNull PasswordEncoder passwordEncoder) {
        this.ownerDao = ownerDao;
        this.catDao = catDao;
        this.userDao = userDao;
        this.ownerParser = ownerParser;
        this.userParser = userParser;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Owner get(@NonNull UUID id) {
        return ownerParser.toDto(ownerDao.findById(id).orElse(null));
    }

    @Override
    public List<Owner> getAll() {
        return ownerDao.findAll().stream().map(ownerParser::toDto).toList();
    }

    @Override
    public void add(@NonNull Owner owner) {
        if (owner.getId() != null) throw new IllegalArgumentException("Id of new owner must be null");
        ownerDao.save(ownerParser.toTransientEntity(owner));
    }

    @Override
    public void delete(@NonNull UUID id) {
        ownerDao.deleteById(id);
    }

    @Override
    @Transactional
    public List<UUID> getCats(@NonNull UUID id) {
        OwnerEntity owner = ownerDao.findById(id).orElse(null);
        return owner != null ? owner.getCats().stream().map(CatEntity::getId).toList() : null;
    }

    @Override
    public void takeCat(@NonNull UUID ownerId, @NonNull UUID catId) {
        OwnerEntity owner = ownerDao.findById(ownerId).orElse(null);
        CatEntity cat = catDao.findById(catId).orElse(null);
        if (owner != null && cat != null && cat.getOwner() == null) {
            cat.setOwner(owner);
            catDao.save(cat);
        }
    }

    @Override
    public void giveCat(@NonNull UUID ownerId, @NonNull UUID catId) {
        OwnerEntity owner = ownerDao.findById(ownerId).orElse(null);
        CatEntity cat = catDao.findById(catId).orElse(null);
        if (owner != null && cat != null && cat.getOwner() == owner) {
            cat.setOwner(null);
            catDao.save(cat);
        }
    }

    @Override
    public void giveCat(@NonNull UUID oldOwnerId, @NonNull UUID newOwnerId, @NonNull UUID catId) {
        OwnerEntity oldOwner = ownerDao.findById(oldOwnerId).orElse(null);
        OwnerEntity newOwner = ownerDao.findById(newOwnerId).orElse(null);
        CatEntity cat = catDao.findById(catId).orElse(null);
        if (oldOwner != null && newOwner != null && cat != null && cat.getOwner() == oldOwner) {
            cat.setOwner(newOwner);
            catDao.save(cat);
        }
    }

    @Override
    public List<Owner> findByName(@NonNull String name) {
        return ownerDao.findByName(name).stream().map(ownerParser::toDto).toList();
    }

    @Override
    public void addUser(@NonNull User user) {
        UserEntity userEntity = userParser.toEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(userEntity);
    }
}
