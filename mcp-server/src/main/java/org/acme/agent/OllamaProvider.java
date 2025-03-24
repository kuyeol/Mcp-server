package org.acme.agent;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;



@ApplicationScoped
public class OllamaProvider
{

  static String MODEL_GEMMA3 = "gemma3:4b"; // try other local ollama model names
  static String MODEL_LLAMA3 = "llama3.2";
  static String MODEL_GROQ   = "llama3-groq-tool-use";
  static String BASE_URL     = "http://182.218.135.247:11434";

  private final ChatLanguageModel languageModel;
  static        String            MODEL_QWEN  = "qwen2.5";
  static        String            MODEL_CODER = "qwen2.5-coder";
  private final ChatLanguageModel qwen;

  private final StreamingChatLanguageModel streamModel;
  private final ChatLanguageModel          coder;
  private final ChatLanguageModel          llama;
  private final ChatLanguageModel          groq;
  private final ChatLanguageModel          granite3;
  private final ChatLanguageModel          phi4;

  public OllamaProvider()
  {
    this.phi4 = OllamaChatModel.builder()
                               .baseUrl(BASE_URL)
                               .modelName("phi4-mini")
                               .temperature(0.0)
                               .timeout(Duration.ofSeconds(60000))
                               .build();

    this.granite3 = OllamaChatModel.builder()
                                   .baseUrl(BASE_URL)
                                   .modelName("granite3-dense:2b")
                                   .temperature(0.0)
                                   .timeout(Duration.ofSeconds(60000))
                                   .build();

    this.groq = OllamaChatModel.builder()
                               .baseUrl(BASE_URL)
                               .modelName(MODEL_GROQ)
                               .temperature(0.0)
                               .timeout(Duration.ofSeconds(60000))
                               .build();


    this.llama = OllamaChatModel.builder()
                                .baseUrl(BASE_URL)
                                .modelName(MODEL_LLAMA3)
                                .temperature(0.0)
                                .timeout(Duration.ofSeconds(60000))
                                .build();

    this.qwen = OllamaChatModel.builder()
                               .baseUrl(BASE_URL)
                               .modelName(MODEL_QWEN)
                               .temperature(0.1)
                               .timeout(Duration.ofSeconds(60000))
                               .build();


    this.coder = OllamaChatModel.builder()
                                .baseUrl(BASE_URL)
                                .modelName(MODEL_CODER)
                                .temperature(0.1)
                                .timeout(Duration.ofSeconds(60000))
                                .build();

    this.languageModel = OllamaChatModel.builder()
                                        .baseUrl(BASE_URL)
                                        .modelName(MODEL_GEMMA3)
                                        .temperature(0.2)
                                        .timeout(Duration.ofSeconds(60000))
                                        .build();

    this.streamModel = OllamaStreamingChatModel.builder()
                                               .baseUrl(BASE_URL)
                                               .modelName(MODEL_GEMMA3)
                                               .temperature(0.0)
                                               .build();
  }

  public ChatLanguageModel getLlama(){
    return this.llama;
  }

  public ChatLanguageModel getPhi4() {
    return this.phi4;
  }

  public ChatLanguageModel getGranite() {
    return this.granite3;
  }

  public ChatLanguageModel getAi() {
    return languageModel;
  }

  public ChatLanguageModel getGroq() {
    return this.groq;
  }


  public ChatLanguageModel providesModel(String modelName) {

    switch (modelName) {
      case "qwen":
        return qwen;
      case "coder":
        return coder;
      default:
        return llama;
    }


  }


  public ChatResponse generateStream(String userMessage)
  {
    CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

    streamModel.chat(userMessage, new StreamingChatResponseHandler()
    {

      @Override
      public void onPartialResponse(String partialResponse)
      {
        System.out.print(partialResponse);
      }


      @Override
      public void onCompleteResponse(ChatResponse completeResponse)
      {
        futureResponse.complete(completeResponse);
      }

      @Override
      public void onError(Throwable error)
      {
        futureResponse.completeExceptionally(error);
      }
    });

    return futureResponse.join();
  }


  public String generateResponse(String userMessage)
  {
    try {
      ChatResponse response = languageModel.chat(ChatRequest.builder().messages(UserMessage.from(userMessage)).build());

      return response.aiMessage().text();

    } catch (Exception e) {
      return "AI 응답 생성 중 오류가 발생했습니다: " + e.getMessage();
    }
  }
}
