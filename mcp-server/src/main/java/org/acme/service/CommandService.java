package org.acme.service;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;

import jakarta.enterprise.context.ApplicationScoped;

import org.acme.agent.Bot;
import org.acme.agent.OllamaProvider;

import java.io.File;
import java.util.List;

import static io.vertx.core.impl.launcher.commands.ExecUtils.addArgument;

@ApplicationScoped
public class CommandService
{ public static final String FILE_TO_BE_READ = "src/main/resources/file.txt";
  static OllamaProvider ollamaProvider = new OllamaProvider();

  static ChatLanguageModel model;

  public CommandService()
  {

    model = ollamaProvider.getMistral();
  }

  private static String token = """
                                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
                                """;

  public String executeCommand()
  throws Exception
  {

//    McpTransport transport = new HttpMcpTransport.Builder()
//      .sseUrl("http://localhost:3001/sse")
//      .logRequests(true) // if you want to see the traffic in the log
//      .logResponses(true)
//      .build();



    McpTransport transport = new StdioMcpTransport.Builder()
      .command(List.of("/usr/bin/npm", "exec",
                       "@modelcontextprotocol/server-filesystem@0.6.2",
                       // allowed directory for the server to interact with
                       new File("src/main/resources").getAbsolutePath()
      ))
      .logEvents(true)
      .build();
    McpClient mcpClient = new DefaultMcpClient.Builder().transport(transport).build();

    ToolProvider toolProvider = McpToolProvider.builder().mcpClients(List.of(mcpClient)).build();

    Bot bot = AiServices.builder(Bot.class).chatLanguageModel(model).toolProvider(toolProvider).build();

    try {
      File file = new File(FILE_TO_BE_READ);
      String response = bot.chat("Read the contents of the file and write java code " + file.getAbsolutePath());
      System.out.println("RESPONSE: " + response);
      return response;
    } finally {
      mcpClient.close();
    }


  }


}
