package org.acme.file.tool;


import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;


@ApplicationScoped
public class FilesystemToolProvider implements Supplier<ToolProvider>
{
  private McpTransport transport;
  private McpClient    mcpClient;
  private ToolProvider toolProvider;


  @Override
  public ToolProvider get() {
    if (toolProvider == null) {
      transport    = new StdioMcpTransport.Builder().command(
        List.of("npm", "exec", "@modelcontextprotocol/server-filesystem@0.6.2",
                // allowed directory for the server to interact with
                new File("playground").getAbsolutePath())).logEvents(true).build();
      mcpClient    = new DefaultMcpClient.Builder().transport(transport).build();
      toolProvider = McpToolProvider.builder().mcpClients(mcpClient).build();
    }
    return toolProvider;
  }
}
