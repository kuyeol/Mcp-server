package org.acme.panache.kafka;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import io.smallrye.reactive.messaging.kafka.reply.KafkaRequestReply;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.panache.record.ProviderRecord;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Message;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import java.util.UUID;

@ApplicationScoped
public class KafkaRequestProducer
{



    @Inject
    @Channel("account-out")
    Emitter<Record<String, ProviderRecord>> emitter;

    public void sendToKafka(ProviderRecord  account) {
        emitter.send(Record.of(UUID.randomUUID().toString(), account));
    }






}