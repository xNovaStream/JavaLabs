package ru.itmo.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.dto.Owner;
import ru.itmo.dto.User;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/owners")
@Validated
public class OwnerController {
    private final RabbitTemplate rabbitTemplate;
    private final String OWNER_EXCHANGE = "ownerExchange";

    @Autowired
    public OwnerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Owner getOwner(@PathVariable UUID id) {
        return (Owner) rabbitTemplate.convertSendAndReceive(OWNER_EXCHANGE, "getOwnerQueue", id);
    }

    @GetMapping
    @PostFilter("hasRole('ADMIN') or filterObject.id == principal.ownerId")
    public List<Owner> getAllOwners() {
        Owner[] result = (Owner[]) rabbitTemplate.convertSendAndReceive(OWNER_EXCHANGE, "getAllOwnersQueue", "");
        return result != null ? Arrays.asList(result) : null;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void addOwner(@RequestBody @Valid Owner owner) {
        rabbitTemplate.convertAndSend(OWNER_EXCHANGE, "addOwnerQueue", owner);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOwner(@PathVariable UUID id) {
        rabbitTemplate.convertAndSend(OWNER_EXCHANGE, "deleteOwnerQueue", id);
    }

    @GetMapping("/{id}/cats")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UUID> getOwnerCats(@PathVariable UUID id) {
        UUID[] result = (UUID[]) rabbitTemplate.convertSendAndReceive(OWNER_EXCHANGE, "getOwnerCatsQueue", id);
        return result != null ? Arrays.asList(result) : null;
    }

    @PatchMapping("/{ownerId}/take/{catId}")
    @PreAuthorize("hasRole('ADMIN') or @СatController.getCat(#catId).ownerId == null")
    public void takeCat(@PathVariable UUID ownerId, @PathVariable UUID catId) {
        rabbitTemplate.convertAndSend(OWNER_EXCHANGE, "takeCatQueue", new UUID[] {ownerId, catId});
    }

    @PatchMapping("/{ownerId}/give/{catId}")
    @PreAuthorize("hasRole('ADMIN') or @СatController.getCat(#catId).ownerId == #ownerId")
    public void giveCat(@PathVariable UUID ownerId, @PathVariable UUID catId) {
        rabbitTemplate.convertAndSend(OWNER_EXCHANGE, "giveCatQueue", new UUID[] {ownerId, catId});
    }

    @PatchMapping("/{oldOwnerId}/give/{catId}/to/{newOwnerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void giveCat(@PathVariable UUID oldOwnerId, @PathVariable UUID newOwnerId, @PathVariable UUID catId) {
        rabbitTemplate.convertAndSend(OWNER_EXCHANGE, "transferCatQueue", new UUID[] {oldOwnerId, newOwnerId, catId});
    }

    @GetMapping("/by-name")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Owner> findOwnersByName(@RequestParam @NotBlank String name) {
        Owner[] result = (Owner[]) rabbitTemplate.convertSendAndReceive(OWNER_EXCHANGE, "findOwnersByNameQueue", name);
        return result != null ? Arrays.asList(result) : null;
    }

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public void addUser(@RequestBody @Valid User user) {
        rabbitTemplate.convertAndSend(OWNER_EXCHANGE, "addUserQueue", user);
    }
}
