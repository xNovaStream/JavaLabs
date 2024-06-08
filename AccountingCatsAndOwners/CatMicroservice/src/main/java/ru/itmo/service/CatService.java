package ru.itmo.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.dao.ICatDao;
import ru.itmo.dto.Cat;
import ru.itmo.dto.parser.ICatParser;
import ru.itmo.entity.CatColor;
import ru.itmo.entity.CatEntity;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

@Service
public class CatService implements ICatService {
    private final ICatDao catDao;
    private final ICatParser catParser;

    @Autowired
    public CatService(@NonNull ICatDao catRepository, @NonNull ICatParser catParser) {
        this.catDao = catRepository;
        this.catParser = catParser;
    }

    @Override
    public Cat get(@NonNull UUID id) {
        return catParser.toDto(catDao.findById(id).orElse(null));
    }

    @Override
    public List<Cat> getAll() {
        return catDao.findAll().stream().map(catParser::toDto).toList();
    }

    @Override
    public void add(@NonNull Cat cat) {
        if (cat.getId() != null) throw new IllegalArgumentException("Id of new cat must be null");
        if (cat.getOwnerId() != null) throw new IllegalArgumentException("Owner id of new cat must be null");
        catDao.save(catParser.toTransientEntity(cat));
    }

    @Override
    public void delete(@NonNull UUID id) {
        catDao.deleteById(id);
    }

    @Override
    @Transactional
    public List<UUID> getFriendIds(@NonNull UUID catId) {
        CatEntity cat = catDao.findById(catId).orElse(null);
        return cat != null ? cat.getFriends().stream().map(CatEntity::getId).toList() : null;
    }

    @Override
    public void makeFriends(@NonNull UUID catId1, @NonNull UUID catId2) {
        changeFriendshipState(catId1, catId2, (cat1, cat2) -> cat1.getFriends().add(cat2));
    }

    @Override
    public void unmakeFriends(@NonNull UUID catId1, @NonNull UUID catId2) {
        changeFriendshipState(catId1, catId2, (cat1, cat2) -> cat1.getFriends().remove(cat2));
    }

    @Override
    public List<Cat> findByName(@NonNull String name) {
        return catDao.findByName(name).stream().map(catParser::toDto).toList();
    }

    @Override
    public List<Cat> findByColor(@NonNull CatColor color) {
        return catDao.findByColor(color).stream().map(catParser::toDto).toList();
    }

    @Override
    public List<Cat> findByBreed(@NonNull String breed) {
        return catDao.findByBreed(breed).stream().map(catParser::toDto).toList();
    }

    private void changeFriendshipState(UUID catId1, UUID catId2, BiConsumer<CatEntity, CatEntity> friendshipOperation) {
        CatEntity cat1 = catDao.findById(catId1).orElse(null);
        CatEntity cat2 = catDao.findById(catId2).orElse(null);
        if (cat1 != null && cat2 != null) {
            friendshipOperation.accept(cat1, cat2);
            friendshipOperation.accept(cat2, cat1);
            catDao.saveAll(List.of(cat1, cat2));
        }
    }
}
