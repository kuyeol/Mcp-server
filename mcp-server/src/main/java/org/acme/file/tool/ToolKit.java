package org.acme.file.tool;

import org.acme.agent.Agentable;

import java.util.HashMap;
import java.util.Map;

public class ToolKit
{

  private final Map<String, Agentable> tools;


  public ToolKit() {
    this.tools = new HashMap<>();
  }

  public void registerTool(Agentable tool) {
    tools.put(tool.toolName(), tool);
  }

  public Agentable getTool(String toolName) {
    return tools.get(toolName);
  }

  public Map<String, Agentable> getAllTools() {
    return tools;
  }





}
