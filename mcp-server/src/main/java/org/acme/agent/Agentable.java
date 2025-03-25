package org.acme.agent;

import dev.langchain4j.agent.tool.Tool;

public interface Agentable
{

  String toolName();

  String executeCommand(Object input);

  String description();

}
