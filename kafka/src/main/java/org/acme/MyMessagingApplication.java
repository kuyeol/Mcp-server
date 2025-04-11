package org.acme;

import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.common.PartitionInfo;
import io.vertx.kafka.client.common.TopicPartition;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@ApplicationScoped
public class MyMessagingApplication
{

    @Inject
    Vertx vertx;

    static Map<String, String> configVx = new HashMap<>();

    KafkaConsumer<String, String> consumer;

    KafkaProducer<String, String> producer;

    @Inject
    @ConfigProperty(name = "kafka.bootstrap.servers")
    String bootstrapServers;

    @Inject
    @ConfigProperty(name = "kafka.group.id")
    String groupId;

    private Map<String, String> createKafkaConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", bootstrapServers);
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", groupId);
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "false");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");
        return config;
    }

    public void createClient() {

        configVx.put("bootstrap.servers", "182.218.135.247:29092");
        configVx.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        configVx.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        configVx.put("group.id", "my_group");
        configVx.put("auto.offset.reset", "earliest");
        configVx.put("enable.auto.commit", "false");
        // Start with a copy of common settings if needed
        configVx.put("key.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
        configVx.put("value.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
        configVx.put("group.id", "my_group");
        configVx.put("auto.offset.reset", "earliest");
        configVx.put("enable.auto.commit", "false");
        configVx.put("key.serializer", "io.vertx.kafka.client.serialization.BufferSerializer");
        configVx.put("value.serializer", "io.vertx.kafka.client.serialization.BufferSerializer");
        configVx.put("acks", "1");
        configVx.put("key.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer");
        configVx.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer");
        configVx.put("acks", "1");

        consumer = KafkaConsumer.create(vertx, createKafkaConfig());

        // subscribe to several topics with lists
        Set<String> topics = new HashSet<>();
        topics.add("topic1");
        topics.add("topic2");
        topics.add("topic3");
        topics.add("test");
        consumer.subscribe(topics)
                .onSuccess(v -> System.out.println("subscribed"))
                .onFailure(cause -> System.out.println("Could not subscribe " + cause.getMessage()));
        // or using a Java regex
        Pattern pattern = Pattern.compile("topic\\d");

        consumer.subscribe(pattern);

        consumer.handler(record -> {
            System.out.println(
                    "Processing key=" + record.key() + ",value=" + record.value() + ",partition=" + record.partition() +
                    ",offset=" + record.offset());
        });
        // or just subscribe to a single topic
        consumer.subscribe("a-single-topic");

        consumer.subscribe("test").onSuccess(v -> {
            System.out.println("Consumer subscribed");

            // Let's poll every second
            vertx.setPeriodic(1000, timerId -> consumer.poll(Duration.ofMillis(100)).onSuccess(records -> {
                for (int i = 0; i < records.size(); i++) {
                    KafkaConsumerRecord<String, String> record = records.recordAt(i);
                    System.out.println(
                            "key=" + record.key() + ",value=" + record.value() + ",partition=" + record.partition() +
                            ",offset=" + record.offset());
                }
            }).onFailure(cause -> {
                System.out.println("Something went wrong when polling " + cause.toString());
                cause.printStackTrace();

                // Stop polling if something went wrong
                vertx.cancelTimer(timerId);
            }));
        });
        consumer.commit().onSuccess(v -> System.out.println("Last read message offset committed"));

        TopicPartition topicPartition2 = new TopicPartition().setTopic("test").setPartition(0);

        // registering the handler for incoming messages
        consumer.handler(record -> {
            System.out.println("key=" + record.key() + ",value=" + record.value() + ",partition=" + record.partition() +
                               ",offset=" + record.offset());

            // i.e. pause/resume on partition 0, after reading message up to offset 5
            if (( record.partition() == 0 ) && ( record.offset() == 5 )) {

                // pause the read operations
                consumer.pause(topicPartition2)
                        .onSuccess(v -> System.out.println("Paused"))
                        .onSuccess(v -> vertx.setTimer(5000, timeId ->
                                // resume read operations
                                consumer.resume(topicPartition2)));
            }

            consumer.close()
                    .onSuccess(v -> System.out.println("Consumer is now closed"))
                    .onFailure(cause -> System.out.println("Close failed: " + cause));
        });
    }

    public void makeProducer() {
        producer = KafkaProducer.create(vertx, configVx);
        //
        for (int i = 0; i < 5; i++) {

            int key = i % 2;

            // only topic and message value are specified, round robin on destination partitions
            KafkaProducerRecord<String, String> record = KafkaProducerRecord.create("test", String.valueOf(key),
                                                                                    "message_", 0);

            producer.send(record)
                    .onSuccess(recordMetadata -> System.out.println(
                            "Message " + record.value() + " written on topic=" + recordMetadata.getTopic() +
                            ", partition=" + recordMetadata.getPartition() + ", offset=" + recordMetadata.getOffset()));
        }

        producer.close()
                .onSuccess(v -> System.out.println("Producer is now closed"))
                .onFailure(cause -> System.out.println("Close failed: " + cause));
    }

    public void createTopic(String topicName) {
        short part = 1;

        consumer = KafkaConsumer.create(vertx, configVx);
        consumer.partitionsFor("test").onSuccess(partitions -> {
            for (PartitionInfo partitionInfo : partitions) {
                System.out.println();
                System.out.println("\t" + partitionInfo);
                System.out.println();
            }
        });
        consumer.listTopics().onSuccess(partitionsTopicMap -> partitionsTopicMap.forEach((topic, partitions) -> {
            System.out.println("\t\n");
            System.out.println("\t topic = " + topic);
            System.out.println("\t");
            System.out.println("\t partitions = \t" + partitions);
            System.out.println("\t");
        }));

        consumer.commit().onSuccess(v -> System.out.println("Last read message offset committed"));

    }

    public String selectTopicFromIncommingMessage(Message<String> incoming) {
        return "string";
    }

    public Message<String> addMetadata(Message<String> incoming) {
        String topicName = selectTopicFromIncommingMessage(incoming);
        OutgoingKafkaRecordMetadata<String> metadata = OutgoingKafkaRecordMetadata.<String>builder()
                                                                                  .withTopic(topicName)
                                                                                  .build();
        return incoming.addMetadata(metadata);
    }




}
