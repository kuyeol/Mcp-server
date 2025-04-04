package org.acme.tutorial;

import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;

public interface AgentToolSupport
{
    ChatResponse send(ChatRequest message);
}
