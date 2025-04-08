package org.acme.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Arrays;
import java.util.Properties;

/**
 * <code>
 * createTopic( ){ }
 * <p>
 * sendTopic( ){ }
 * </code>
 */

@ApplicationScoped
public class KafkaPublisher
{



    @Inject
    @ConfigProperty(name = "kafka.bootstrap.servers")
    private String bootstrapServer;


    final StreamsBuilder builder = new StreamsBuilder();

    final Topology topology = builder.build();

    public void createTopicByAdmin() {

    }








}
