package ru.itmo.rabbit;

import lombok.NonNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.dto.Owner;
import ru.itmo.dto.User;
import ru.itmo.service.IOwnerService;

import java.util.UUID;

@Component
public class RabbitReceiver {
    private final IOwnerService ownerService;

    @Autowired
    public RabbitReceiver(@NonNull IOwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @RabbitListener(queues = "getOwnerQueue")
    public Owner processGetOwnerMessage(UUID id) {
        return ownerService.get(id);
    }

    @RabbitListener(queues = "getAllOwnersQueue")
    public Owner[] processGetAllOwnersMessage(String ignoredMessage) {
        return ownerService.getAll().toArray(new Owner[0]);
    }

    @RabbitListener(queues = "addOwnerQueue")
    public void processAddOwnerMessage(Owner owner) {
        ownerService.add(owner);
    }

    @RabbitListener(queues = "deleteOwnerQueue")
    public void processDeleteOwnerMessage(UUID id) {
        ownerService.delete(id);
    }

    @RabbitListener(queues = "getOwnerCatsQueue")
    public UUID[] processGetOwnerCatsMessage(UUID id) {
        return ownerService.getCats(id).toArray(new UUID[0]);
    }

    @RabbitListener(queues = "takeCatQueue")
    public void processTakeCatMessage(UUID[] ids) {
        ownerService.takeCat(ids[0], ids[1]);
    }

    @RabbitListener(queues = "giveCatQueue")
    public void processGiveCatMessage(UUID[] ids) {
        ownerService.giveCat(ids[0], ids[1]);
    }

    @RabbitListener(queues = "transferCatQueue")
    public void processTransferCatMessage(UUID[] ids) {
        ownerService.giveCat(ids[0], ids[1], ids[2]);
    }

    @RabbitListener(queues = "findOwnersByNameQueue")
    public Owner[] processFindOwnersByNameMessage(String name) {
        return ownerService.findByName(name).toArray(new Owner[0]);

    }

    @RabbitListener(queues = "addUserQueue")
    public void processAddUserMessage(User user) {
        ownerService.addUser(user);
    }
}
