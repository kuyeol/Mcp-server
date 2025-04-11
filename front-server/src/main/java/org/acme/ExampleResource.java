package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.service.CommandService;
import org.acme.tutorial.DefaultService;

import java.util.List;

@ApplicationScoped
@Path("/hello")
public class ExampleResource
{

    private final DefaultService defaultService;

    public ExampleResource(DefaultService defaultService) {
        this.defaultService = defaultService;
    }

    @Inject
    CommandService commandService;

    @POST
    @Path("/commandExe")
    @Produces(MediaType.TEXT_PLAIN)
    public Response post(String arg)
    throws Exception
    {
        String rep = commandService.executeCommand(arg);
        System.out.println(rep);
        return Response.ok(rep).build();
    }

    @GET
    @Path("defaultRequest{arg}")
    public Response defaultService(
            @PathParam("arg")
            String arg)
    {

        String chat = defaultService.requestMessage(arg);
        return Response.ok().entity(chat).build();

    }

    @GET
    @Path("repeatRequestMessages{async}")
    public Response loop(String async) {

        List<String> m = defaultService.repeatRequestMessages(async);

        return Response.ok().entity(m).build();
    }




}
