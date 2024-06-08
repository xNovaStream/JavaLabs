package ru.itmo.rabbit;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean("CatExchange")
    public DirectExchange catExchange() {
        return new DirectExchange("catExchange");
    }
    @Bean("OwnerExchange")
    public DirectExchange ownerExchange() {
        return new DirectExchange("ownerExchange");
    }
    @Bean
    public Declarables topicBindings(@Qualifier("CatExchange") DirectExchange catExchange) {
        Queue getCatQueue = new Queue("getCatQueue");
        Queue getAllCatsQueue = new Queue("getAllCatsQueue");
        Queue addCatQueue = new Queue("addCatQueue");
        Queue deleteCatQueue = new Queue("deleteCatQueue");
        Queue getFriendsQueue = new Queue("getFriendsQueue");
        Queue makeFriendsQueue = new Queue("makeFriendsQueue");
        Queue unmakeFriendsQueue = new Queue("unmakeFriendsQueue");
        Queue findByColorQueue = new Queue("findByColorQueue");
        Queue findByNameQueue = new Queue("findByNameQueue");
        Queue findByBreedQueue = new Queue("findByBreedQueue");

        return new Declarables(
                getCatQueue, getAllCatsQueue, addCatQueue, deleteCatQueue, getFriendsQueue,
                makeFriendsQueue, unmakeFriendsQueue, findByColorQueue, findByNameQueue,
                findByBreedQueue, catExchange,
                BindingBuilder.bind(getCatQueue).to(catExchange).withQueueName(),
                BindingBuilder.bind(getAllCatsQueue).to(catExchange).withQueueName(),
                BindingBuilder.bind(addCatQueue).to(catExchange).withQueueName(),
                BindingBuilder.bind(deleteCatQueue).to(catExchange).withQueueName(),
                BindingBuilder.bind(getFriendsQueue).to(catExchange).withQueueName(),
                BindingBuilder.bind(makeFriendsQueue).to(catExchange).withQueueName(),
                BindingBuilder.bind(unmakeFriendsQueue).to(catExchange).withQueueName(),
                BindingBuilder.bind(findByColorQueue).to(catExchange).withQueueName(),
                BindingBuilder.bind(findByNameQueue).to(catExchange).withQueueName(),
                BindingBuilder.bind(findByBreedQueue).to(catExchange).withQueueName()
        );
    }

    @Bean
    public Declarables ownerBindings(@Qualifier("OwnerExchange") DirectExchange ownerExchange) {
        Queue getOwnerQueue = new Queue("getOwnerQueue");
        Queue getAllOwnersQueue = new Queue("getAllOwnersQueue");
        Queue addOwnerQueue = new Queue("addOwnerQueue");
        Queue deleteOwnerQueue = new Queue("deleteOwnerQueue");
        Queue getOwnerCatsQueue = new Queue("getOwnerCatsQueue");
        Queue takeCatQueue = new Queue("takeCatQueue");
        Queue giveCatQueue = new Queue("giveCatQueue");
        Queue transferCatQueue = new Queue("transferCatQueue");
        Queue findOwnersByNameQueue = new Queue("findOwnersByNameQueue");
        Queue addUserQueue = new Queue("addUserQueue");

        return new Declarables(
                getOwnerQueue, getAllOwnersQueue, addOwnerQueue, deleteOwnerQueue, getOwnerCatsQueue,
                takeCatQueue, giveCatQueue, transferCatQueue, findOwnersByNameQueue, addUserQueue,
                ownerExchange,
                BindingBuilder.bind(getOwnerQueue).to(ownerExchange).withQueueName(),
                BindingBuilder.bind(getAllOwnersQueue).to(ownerExchange).withQueueName(),
                BindingBuilder.bind(addOwnerQueue).to(ownerExchange).withQueueName(),
                BindingBuilder.bind(deleteOwnerQueue).to(ownerExchange).withQueueName(),
                BindingBuilder.bind(getOwnerCatsQueue).to(ownerExchange).withQueueName(),
                BindingBuilder.bind(takeCatQueue).to(ownerExchange).withQueueName(),
                BindingBuilder.bind(giveCatQueue).to(ownerExchange).withQueueName(),
                BindingBuilder.bind(transferCatQueue).to(ownerExchange).withQueueName(),
                BindingBuilder.bind(findOwnersByNameQueue).to(ownerExchange).withQueueName(),
                BindingBuilder.bind(addUserQueue).to(ownerExchange).withQueueName()
        );
    }
}
