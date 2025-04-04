package org.acme.panache.kafka;

import io.smallrye.reactive.messaging.kafka.reply.KafkaRequestReply;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;
import java.util.Random;
import io.smallrye.reactive.messaging.kafka.Record;

@ApplicationScoped
public class RequestConsumer
{
    private final Logger logger = Logger.getLogger(RequestConsumer.class);


    @Incoming("account-in")
    public void receive(Record<String, ProducerRecord> record) {

//        logger.infof("Got a record %s: name: %s - email: %s ", record.key(), record.value(), record.value());
 ProducerRecord rr = record.value();
        System.out.println(rr.value().toString());

    }






}
