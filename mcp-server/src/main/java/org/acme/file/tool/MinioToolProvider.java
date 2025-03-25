package org.acme.file.tool;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.acme.agent.Agentable;
import org.acme.agent.OllamaProvider;


public class MinioToolProvider
{

  private static ChatLanguageModel model = new OllamaProvider().getPhi4();
  private final  ToolKit           toolKit;

  public MinioToolProvider(ToolKit toolKit) {
    this.toolKit = toolKit;
  }

  public String processUserRequest(String userQuery) {
    // ToolProvider에서 모든 도구 가져오기// 대화 기록 (선택 사항)
    MinioAgent agent = AiServices.builder(MinioAgent.class)
                                 .chatLanguageModel(model)
                                 .tools(toolKit.getAllTools().values().toArray(new Agentable[0]))
                                 .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                                 .build();

    // 2. 에이전트 실행
    return agent.chat(userQuery);
  }


}
