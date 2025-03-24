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
import org.acme.agent.GithubProvider;
import org.acme.agent.OllamaProvider;

import java.util.List;

@ApplicationScoped
public class CommandService
{
  static OllamaProvider ollamaProvider = new OllamaProvider();

  static ChatLanguageModel model;
  static ChatLanguageModel gpt4o;

  public CommandService()
  {
    this.model = ollamaProvider.getLlama();
    this.gpt4o = new GithubProvider().getGPT4o();
  }

  private final static String GIT_TOKEN
    = "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";

  public String executeCommand()
  throws Exception
  {

    McpTransport transport = new StdioMcpTransport.Builder().command(
      List.of("/usr/local/bin/docker", "run", "-e", GIT_TOKEN, "-i", "mcp/github")).logEvents(true).build();

    McpClient mcpClient = new DefaultMcpClient.Builder().transport(transport).build();

    ToolProvider toolProvider = McpToolProvider.builder().mcpClients(List.of(mcpClient)).build();

    Bot bot = AiServices.builder(Bot.class).chatLanguageModel(gpt4o).toolProvider(toolProvider).build();

    try {
      String response = bot.chat("Summarize the last 3 commits of the LangChain4j GitHub repository");
      System.out.println("RESPONSE: " + response);
      return response;
    } finally {
      mcpClient.close();
    }


  }


}
