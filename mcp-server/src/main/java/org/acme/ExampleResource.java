package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.service.CommandService;


@ApplicationScoped
@Path("/hello")
public class ExampleResource
{


  @Inject
  CommandService commandService;


  public ExampleResource() {

  }


  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String hello() {
    return "Hello from Quarkus REST";
  }


  @POST
  @Path("/command")
  @Produces(MediaType.TEXT_PLAIN)
  public Response post(String arg)
  throws Exception
  {

    String rep = commandService.executeCommand(arg);
    System.out.println(rep);
    return Response.ok(rep).build();
  }


}
