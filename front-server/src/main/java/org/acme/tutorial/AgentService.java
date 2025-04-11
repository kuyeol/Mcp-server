package org.acme.tutorial;

import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import jakarta.inject.Singleton;


public interface AgentService
{


 String requests(String message);
 ChatResponse send(ChatRequest message);


}
