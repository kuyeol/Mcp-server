package org.acme;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.common.annotation.Identifier;
import io.smallrye.reactive.messaging.kafka.KafkaClientService;
import io.smallrye.reactive.messaging.kafka.KafkaProducer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.reactive.messaging.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import org.apache.kafka.clients.producer.ProducerRecord;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.kafka.KafkaClientService;
import io.smallrye.reactive.messaging.kafka.KafkaConsumer;
import io.smallrye.reactive.messaging.kafka.KafkaProducer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
public class MyMessagingApplication
{

    @Inject
    @Channel("words-out")
    Emitter<String> emitter;

    /**
     * Sends message to the "words-out" channel, can be used from a JAX-RS resource or any bean of your application.
     * Messages are sent to the broker.
     **/
    void onStart(
            @Observes
            StartupEvent ev)
    {
        Stream.of("Hello", "with", "Quarkus", "Messaging", "message").forEach(string -> emitter.send(string));
        createTopic(ev.toString());
    }

    /**
     * Consume the message from the "words-in" channel, uppercase it and send it to the uppercase channel.
     * Messages come from the broker.
     **/
    @Incoming("words-in")
    @Outgoing("uppercase")
    public Message<String> toUpperCase(Message<String> message) {
        return message.withPayload(message.getPayload().toUpperCase());
    }

    /**
     * Consume the uppercase channel (in-memory) and print the messages.
     **/
    @Incoming("uppercase")
    public void sink(String word) {
        System.out.println(">> " + word);
    }

    @Inject
    @Identifier("default-kafka-broker")
    Map<String, Object> config;

    @Produces
    AdminClient getAdmin() {
        Map<String, Object> copy = new HashMap<>();
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            if (AdminClientConfig.configNames().contains(entry.getKey())) {
                copy.put(entry.getKey(), entry.getValue());
            }
        }
        return KafkaAdminClient.create(copy);
    }

    public void createTopic(NewTopic topic) {
        NewTopic newTopic = new NewTopic(topic.name(), topic.numPartitions(), topic.replicationFactor());
        Collection<NewTopic> topics = List.of(newTopic);
        getAdmin().createTopics(topics);
    }

public void createTopic(String topicName){
        short part =1;
    NewTopic topic = new NewTopic(topicName, part, (short) 1);
    Collection<NewTopic> topics = List.of(topic);
    getAdmin().createTopics(topics);

}

}
