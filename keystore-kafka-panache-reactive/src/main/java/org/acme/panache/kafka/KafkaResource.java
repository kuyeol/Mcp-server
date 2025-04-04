package org.acme.panache.kafka;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.panache.dao.UserDao;
import org.acme.panache.exception.ModelException;
import org.acme.panache.record.ProviderRecord;
import org.acme.panache.record.UserRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Path("/account")
public class KafkaResource
{

    @Inject
    KafkaRequestProducer producer;

    @Inject
    UserDao userDao;


    @POST
    @Path("{name}")
    public Response send(@PathParam("name") String name) {

        try {
            UserRecord     ur  = userDao.findUserByName(name);
            String         id  = ur.id();
            ProviderRecord rec = userDao.findProviderByUserId(id).stream().findFirst().orElse(null);
            producer.sendToKafka(rec);
            return Response.status(Response.Status.OK).entity(ur).build();

        } catch (ModelException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }






}
