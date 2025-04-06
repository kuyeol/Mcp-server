package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.file.SourceUpload;
import org.acme.kafka.KafkaPublisher;

@Path("/hello")
@ApplicationScoped
public class GreetingResource {

    @Inject
    SourceUpload sourceUpload;

    KafkaPublisher kPublisher;

    GreetingResource(KafkaPublisher KafkaPublisher) {
        this.kPublisher = KafkaPublisher;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        sourceUpload.setBucket("aidata", "object");
        sourceUpload.uploadFile("qsssurssssskus", "fil1e.txt");

        return "Hello from Quarkus REST";
    }

    @GET
    @Path("/ka")
    public void topicSend() {

        kPublisher.setKafka();
    }

}
