package org.acme;

import dev.langchain4j.model.chat.ChatLanguageModel;
import io.minio.MinioAsyncClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.agent.OllamaProvider;
import org.acme.file.minio.MinioMakeFile;
import org.acme.file.tool.ToolKit;
import org.acme.service.CommandService;
import org.acme.tutorial.DefaultService;

import java.util.List;

@ApplicationScoped
@Path("/hello")
public class ExampleResource
{

    @Inject
    MinioAsyncClient minioClient;

    private final DefaultService defaultService;

    public ExampleResource(DefaultService defaultService) {
        this.defaultService = defaultService;
    }

    @Inject
    ToolKit toolBox() {
        ToolKit toolKit = new ToolKit();
        toolKit.registerTool(new MinioMakeFile(minioClient));

        return toolKit;
    }

    @Inject
    CommandService commandService;

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

    @POST
    @Path("/minio")
    public Response minioAgent() {

        ChatLanguageModel model = new OllamaProvider().getLlama();
        //    ToolProvider   toolProvider = McpToolProvider.builder().mcpClients(List.of(toolBox())).build();

        //    MinioAgent        bot          = AiServices.builder(Bot.class).chatLanguageModel(model).toolProvider(toolBox()).build();

        return Response.ok().build();

    }

    @GET
    @Path("defaultService{arg}")
    public Response defaultService(
            @PathParam("arg")
            String arg)
    {

        String chat = defaultService.requests(arg);
        return Response.ok().entity(chat).build();

    }

    @GET
    @Path("loop{async}")
    public Response loop(String async) {

        List<String> m = defaultService.asyncMessages(async);

        return Response.ok().entity(m).build();
    }




}
