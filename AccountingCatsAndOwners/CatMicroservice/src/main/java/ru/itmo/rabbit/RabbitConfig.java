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
}
