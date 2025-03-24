package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.service.CommandService;

@Path("/hello")
public class ExampleResource
{


  private  static CommandService commandService;

  public ExampleResource() {
    this.commandService = new CommandService();
  }


  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String hello() {
    return "Hello from Quarkus REST";
  }


  @POST
  @Path("/post")
  @Produces(MediaType.TEXT_PLAIN)
  public Response post()
  throws Exception
  {

   String rep =  commandService.executeCommand();
    return Response.ok(rep).build();
  }


}
