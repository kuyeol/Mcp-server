package org.acme;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import io.minio.MinioAsyncClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.agent.Bot;
import org.acme.agent.OllamaProvider;
import org.acme.file.minio.MinioMakeFile;
import org.acme.file.tool.MinioAgent;
import org.acme.file.tool.ToolKit;
import org.acme.service.CommandService;

import java.util.List;


@ApplicationScoped
@Path("/hello")
public class ExampleResource
{


  @Inject
  MinioAsyncClient minioClient;


  @Inject
  ToolKit toolBox() {
    ToolKit toolKit = new ToolKit();
    toolKit.registerTool(new MinioMakeFile(minioClient));

    return toolKit;
  }

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


  @POST
  @Path("/minio")
  public Response minioAgent(){

    ChatLanguageModel model        = new OllamaProvider().getLlama();
//    ToolProvider   toolProvider = McpToolProvider.builder().mcpClients(List.of(toolBox())).build();
//
//    MinioAgent        bot          = AiServices.builder(Bot.class).chatLanguageModel(model).toolProvider(toolBox()).build();

    return Response.ok().build();


  }



}
