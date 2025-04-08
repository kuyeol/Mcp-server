package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.apache.kafka.clients.admin.NewTopic;

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
        NewTopic newTopic  = new NewTopic(topic, topic.length(), (short) topic.length());
        application.createTopic(newTopic.name());

        return Response.ok("newTopic").build();

    }




}
