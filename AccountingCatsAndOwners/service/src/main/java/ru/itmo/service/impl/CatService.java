package ru.itmo.service.impl;

import lombok.NonNull;
import ru.itmo.dao.ICatDao;
import ru.itmo.dto.Cat;
import ru.itmo.dto.parser.ICatParser;
import ru.itmo.entity.CatEntity;
import ru.itmo.service.ICatService;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class CatService implements ICatService {
    private final ICatDao catDao;
    private final ICatParser catParser;

    public CatService(@NonNull ICatDao catRepository, @NonNull ICatParser catParser) {
        this.catDao = catRepository;
        this.catParser = catParser;
    }

    @Override
    public Cat get(@NonNull UUID id) {
        return catParser.toDto(catDao.findById(id));
    }

    @Override
    public List<Cat> getAll() {
        return catDao.findAll().stream().map(catParser::toDto).toList();
    }

    @Override
    public void add(@NonNull Cat cat) {
        catDao.save(catParser.toTransientEntity(cat));
    }

    @Override
    public void delete(@NonNull UUID id) {
        catDao.delete(id);
    }

    @Override
    public List<UUID> getFriendIds(@NonNull UUID catId) {
        List<CatEntity> friends = catDao.getFriendIds(catId);
        return friends != null ? friends.stream().map(CatEntity::getId).toList() : null;
    }

    @Override
    public void makeFriends(@NonNull UUID catId1, @NonNull UUID catId2) {
        changeFriendshipState(catId1, catId2, (cat1, cat2) -> cat1.getFriends().add(cat2));
    }

    @Override
    public void unmakeFriends(@NonNull UUID catId1, @NonNull UUID catId2) {
        changeFriendshipState(catId1, catId2, (cat1, cat2) -> cat1.getFriends().remove(cat2));
    }

    private void changeFriendshipState(UUID catId1, UUID catId2, BiConsumer<CatEntity, CatEntity> friendshipOperation) {
        CatEntity cat1 = catDao.findById(catId1);
        CatEntity cat2 = catDao.findById(catId2);
        if (cat1 != null && cat2 != null) {
            friendshipOperation.accept(cat1, cat2);
            friendshipOperation.accept(cat2, cat1);
            catDao.updateAll(cat1, cat2);
        }
    }
}
