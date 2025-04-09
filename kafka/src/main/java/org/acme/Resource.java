package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.apache.kafka.clients.admin.NewTopic;
import org.eclipse.microprofile.reactive.messaging.Message;

@Path("/resource")
public class Resource
{

    @Inject
    MyMessagingApplication application;

    @GET
    @Path("/{topic}")
    public Response test(String topic) {

        application.createTopic(topic);

        return Response.ok("Hello World!").build();

    }

    @POST
    @Path("/createTopic")

    public Response test1(String topic) {

        application.createClient();

        return Response.ok("newTopic").build();

    }


    @GET
    @Path("makeProducer")
    public void producer(){

        application.makeProducer();

    }



}
