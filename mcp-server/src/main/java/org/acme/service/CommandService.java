package org.acme.service;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.agent.Bot;
import org.acme.agent.OllamaProvider;

import java.util.List;

@ApplicationScoped
public class CommandService
{
  static OllamaProvider    ollamaProvider = new OllamaProvider();

  static ChatLanguageModel model;

  public CommandService()
  {

     model = ollamaProvider.getPhi4();
  }

  private static String token = """
                                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
                                """;

  public String executeCommand()
  throws Exception
  {

    McpTransport transport = new StdioMcpTransport.Builder().command(
      List.of("/usr/local/bin/docker", "run", "-e", token, "-i", "mcp/github")).logEvents(true).build();

    McpClient mcpClient = new DefaultMcpClient.Builder().transport(transport).build();

    ToolProvider toolProvider = McpToolProvider.builder().mcpClients(List.of(mcpClient)).build();

    Bot bot = AiServices.builder(Bot.class).chatLanguageModel(model).toolProvider(toolProvider).build();

    try {
      String response = bot.chat(
        "repo_owner : zimop12@gmail.com , repo_name : mcp-server Summarize the last 30 commits of the this repository");
      System.out.println("RESPONSE: " + response);
      return response;
    } finally {
      mcpClient.close();
    }


  }


}
