package org.acme.panache;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.acme.panache.dao.UserDao;
import org.acme.panache.exception.ModelException;
import org.acme.panache.record.ProviderRecord;
import org.acme.panache.record.UserRecord;

import java.util.List;

@Path("/prices")
@ApplicationScoped
public class PriceResource
{

    private final UserDao providerDao;

    public PriceResource(UserDao providerDAO)
    {
        this.providerDao = providerDAO;
        init();
    }

    @GET
    @Path("INIT")
    public void init() {

        UserRecord userRecord = new UserRecord("", "test");
        try {
            registerUser(userRecord);
            addProvider(userRecord.name(), new ProviderRecord("", "test", "test", "test"));
            addProvider(userRecord.name(), new ProviderRecord("", "test", "1test", "test"));
        } catch (ModelException e) {
            throw new ModelException(String.valueOf(e));
        }

    }

    @GET
    @Path("/user{name}")
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
    @Path("/user{userid}")
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

    @GET
    @Path("/provider{name}")
    public Response getProviderName(
            @PathParam("name")
            String name)
    {
        try {
            List<ProviderRecord> provider = providerDao.findByProviderName(name);
            return Response.status(Response.Status.OK).entity(provider).build();

        } catch (ModelException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }

    }


//    @GET
//    @Path("/provider{user}")
//    public Response getProviderByUser(
//            @PathParam("user")
//            String user)
//    {
//        try {
//
//            return Response.status(Response.Status.OK).entity(provider).build();
//
//        } catch (ModelException e) {
//            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
//        }
//
//    }


    @POST
    @Path("/registeruser")
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
    @Path("/{userId}addprovider")
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




}
