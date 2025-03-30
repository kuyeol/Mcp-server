package org.acme.panache;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.panache.dao.UserDao;
import org.acme.panache.exception.ModelException;
import org.acme.panache.record.ProviderRecord;
import org.acme.panache.record.UserRecord;

import java.util.List;

@Path("/")
@ApplicationScoped
public class PriceResource
{

    private final UserDao providerDao;

    public PriceResource(UserDao providerDAO)
    {
        this.providerDao = providerDAO;
    }

    public String init(
            @Observes
            StartupEvent event)
    {
        UserRecord userRecord  = new UserRecord("", "test1");
        UserRecord userRecord2 = new UserRecord("", "test2");
        try {
            registerUser(userRecord);
            //
            registerUser(userRecord2);
            //
            addProvider(userRecord.name(), new ProviderRecord("", "test", "test", "test"));
            addProvider(userRecord.name(), new ProviderRecord("", "test", "1test", "test"));
            addProvider(userRecord.name(), new ProviderRecord("", "test", "11test", "test"));
            //
            addProvider(userRecord2.name(), new ProviderRecord("", "test", "test", "test"));
            addProvider(userRecord2.name(), new ProviderRecord("", "test", "1test", "test"));
            addProvider(userRecord2.name(), new ProviderRecord("", "test", "11test", "test"));
            return "";
        } catch (ModelException e) {
            throw new ModelException(String.valueOf(e));
        }
    }

    @GET
    @Path("/providerFecth{user}")
    public Response getProvider(
            @PathParam("user")
            String user)
    {

        List<ProviderRecord> agentProvider = providerDao.findProviderByUserId(user);
        return Response.ok(agentProvider).build();
    }

    @GET
    @Path("/userFindByName/{name}")
    public Response getUserName(
            @PathParam("name")
            String name)
    {
        try {
            UserRecord user = providerDao.findUserByName(name);
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (ModelException e) {

            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }

    }

    @GET
    @Path("/userFindById{userid}")
    public Response getUserId(
            @PathParam("userid")
            String userid)
    {
        try {
            UserRecord re = providerDao.findUserById(userid);
            return Response.status(Response.Status.OK).entity(re).build();

        } catch (ModelException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }

    }

    @POST
    @Path("/user/register")
    public Response registerUser(UserRecord user)
    {
        try {
            user = providerDao.registerUser(user);
            return Response.status(Response.Status.CREATED)
                           .entity("User registered successfully.\n" + user.name())
                           .build();

        } catch (ModelException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }

    }

    @POST
    @Path("/provider/addprovider{userId}")
    public Response addProvider(
            @PathParam("userId")
            String userName, ProviderRecord provider)
    {
        try {
            provider = providerDao.addProvider(provider, userName);
            return Response.status(Response.Status.CREATED).entity("successfully.\n" + provider).build();
        } catch (ModelException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }

    }

    @DELETE
    @Path("{providerName}")
    public void deleteProvider(String userId, String providerName) {

        providerDao.deleteProvider(userId, providerName);

    }




}
