package org.acme.panache;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.acme.panache.dao.ProviderDAO;
import org.acme.panache.entity.ModelInfo;
import org.acme.panache.entity.ModelProvider;

@Path("/prices")
@ApplicationScoped
public class PriceResource
{


  private final ProviderDAO providerDao;


  public PriceResource(ProviderDAO providerDAO) {
    this.providerDao = providerDAO;

  }

  @GET
  public Response get() {


    return Response.ok().build();
  }


  @POST
  @Path("/addModel")
  public Response addModelInfo(String STR) {


    ModelProvider provider = providerDao.addProvider("da");


    return Response.ok(provider).build();
  }


}
