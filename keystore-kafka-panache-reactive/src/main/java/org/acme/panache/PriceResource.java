package org.acme.panache;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.acme.panache.dao.ProviderDAO;
import org.acme.panache.record.ProviderRecord;
import org.acme.panache.record.UserRecord;

@Path("/prices")
@ApplicationScoped
public class PriceResource
{
  private final ProviderDAO providerDao;

  public PriceResource(ProviderDAO providerDAO)
  {
    this.providerDao = providerDAO;
  }


  @GET
  public Response get()
  {
    UserRecord re = providerDao.findUser("1");

    return Response.ok(re)
                   .build();
  }

  @POST
  @Path("/registeruser")
  public Response registerUser(UserRecord user)
  {

    user = providerDao.registerUser(user);

    return Response.ok(user)
                   .build();
  }

  @POST
  @Path("/{userId}addprovider")
  public Response addProvider(@PathParam("userId") String userId,
                              ProviderRecord provider)
  {


    provider = providerDao.addProvider(provider,
                                       userId);

    return Response.ok(provider)
                   .build();
  }


}
