package ru.itmo.rabbit;

import lombok.NonNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.dto.Cat;
import ru.itmo.entity.CatColor;
import ru.itmo.service.ICatService;

import java.util.UUID;

@Component
public class RabbitReceiver {
    private final ICatService catService;

    @Autowired
    RabbitReceiver(@NonNull ICatService catService) {
        this.catService = catService;
    }

    @RabbitListener(queues = "getCatQueue")
    public Cat processGetCatMessage(UUID id) {
        return catService.get(id);
    }

    @RabbitListener(queues = "getAllCatsQueue")
    public Cat[] processGetAllCatsMessage(String ignoredMessage) {
        return catService.getAll().toArray(new Cat[0]);
    }

    @RabbitListener(queues = "addCatQueue")
    public void processAddCatMessage(Cat cat) {
        catService.add(cat);
    }

    @RabbitListener(queues = "deleteCatQueue")
    public void processDeleteCatMessage(UUID id) {
        catService.delete(id);
    }

    @RabbitListener(queues = "getFriendsQueue")
    public UUID[] processGetFriendsMessage(UUID id) {
        return catService.getFriendIds(id).toArray(new UUID[0]);
    }

    @RabbitListener(queues = "makeFriendsQueue")
    public void processMakeFriendsMessage(UUID[] ids) {
        catService.makeFriends(ids[0], ids[1]);
    }

    @RabbitListener(queues = "unmakeFriendsQueue")
    public void processUnmakeFriendsMessage(UUID[] ids) {
        catService.unmakeFriends(ids[1], ids[2]);
    }

    @RabbitListener(queues = "findByColorQueue")
    public Cat[] processFindByColorMessage(CatColor color) {
        return catService.findByColor(color).toArray(new Cat[0]);
    }

    @RabbitListener(queues = "findByNameQueue")
    public Cat[] processFindByNameMessage(String name) {
        return catService.findByName(name).toArray(new Cat[0]);
    }

    @RabbitListener(queues = "findByBreedQueue")
    public Cat[] processFindByBreedMessage(String breed) {
        return catService.findByBreed(breed).toArray(new Cat[0]);
    }
}
