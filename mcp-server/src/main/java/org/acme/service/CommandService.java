package org.acme.service;


import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.agent.Bot;
import org.acme.agent.OllamaProvider;

import java.util.List;

@ApplicationScoped
public class CommandService
{
  static OllamaProvider ollamaProvider;

  static ChatLanguageModel model;


  public CommandService()
  {
    this.model = ollamaProvider.providesModel("llama");

  }

  public void executeCommand()
  throws Exception
  {

    McpTransport transport = new StdioMcpTransport.Builder().command(
                                                              List.of("/usr/local/bin/docker", "run", "-e", "GITHUB_PERSONAL_ACCESS_TOKEN", "-i", "mcp/github"))
                                                            .logEvents(true)
                                                            .build();

    McpClient mcpClient = new DefaultMcpClient.Builder().transport(transport).build();

    ToolProvider toolProvider = McpToolProvider.builder().mcpClients(List.of(mcpClient)).build();

    Bot bot = AiServices.builder(Bot.class).chatLanguageModel(model).toolProvider(toolProvider).build();

    try {
      String response = bot.chat("Summarize the last 3 commits of the LangChain4j GitHub repository");
      System.out.println("RESPONSE: " + response);
    } finally {
      mcpClient.close();
    }


  }


}
