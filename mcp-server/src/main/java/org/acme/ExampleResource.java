package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
  public String post()
  throws Exception
  {

    commandService.executeCommand();
    return "Hello from Quarkus REST";
  }


}
