package org.acme.tutorial;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.agent.Bot;
import org.acme.agent.GeminiFactory;
import org.acme.agent.OllamaProvider;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class DefaultService
{

    @Inject
    OllamaProvider ollamaProvider;

    @Inject
    GeminiFactory geminiProvider;

    private AgentService serviceBuild() {
        return AiServices.builder(AgentService.class)
                         .chatLanguageModel(geminiProvider.getGemini2())
                         .tools(new ToolSupport())
                         .build();
    }

    public static ToolSupport myTools = new ToolSupport();

    public String requests(String message) {
        ChatLanguageModel gemini = geminiProvider.getGeminiLite();

        ChatLanguageModel toolAgent = ollamaProvider.getNemotron();
        AgentService agentService = AiServices.builder(AgentService.class)
                                              .chatLanguageModel(gemini)
                                              .tools(myTools)
                                              .build();

        return agentService.requests(message);

    }

    public AiMessage aiResponse(String message) {

        ChatLanguageModel gemini    = geminiProvider.getGeminiLite();
        ChatLanguageModel mistral   = ollamaProvider.getMistral();
        ChatLanguageModel toolAgent = ollamaProvider.getNemotron();

        AgentToolSupport agentService = AiServices.builder(AgentToolSupport.class)
                                                  .chatLanguageModel(toolAgent)
                                                  .tools(myTools)
                                                  .build();

        ChatRequest request = ChatRequest.builder()
                                         .messages(
                                                 UserMessage.from("What will the weather be like in London tomorrow?"))
                                         .build();

        ChatResponse response = agentService.send(request);

        return response.aiMessage();
    }

    public List<String> asyncMessages(String message) {
        ChatLanguageModel gemini = geminiProvider.getGeminiLite();

        AgentService agentService = AiServices.builder(AgentService.class)
                                              .chatLanguageModel(gemini)
                                              .tools(myTools)
                                              .build();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
        List<CompletableFuture<String>> futures = IntStream.range(0, 10).mapToObj(i -> {
            CompletableFuture<String> future = new CompletableFuture<>();
            scheduler.schedule(() -> future.complete(agentService.requests(message + i)), i * 500,
                               TimeUnit.MILLISECONDS); // 500ms 간격으로 실행
            return future;
        }).collect(Collectors.toList());

        // 모든 요청이 완료될 때까지 기다리고 결과 수집
        List<String> responses = futures.stream().map(CompletableFuture :: join).collect(Collectors.toList());

        return responses;
    }

    public static final String FILE_TO_BE_READ = "src/main/resources/file.txt";

    public static void main(String[] args)
    throws Exception
    {

        ChatLanguageModel model = new GeminiFactory().getGemini2();

        McpTransport transport = new StdioMcpTransport.Builder().command(List.of("file.txt")).build();

        McpClient mcpClient = new DefaultMcpClient.Builder().transport(transport).build();

        ToolProvider toolProvider = McpToolProvider.builder().mcpClients(List.of(mcpClient)).build();

        Bot bot = AiServices.builder(Bot.class).chatLanguageModel(model).toolProvider(toolProvider).build();

        try {
            File   file     = new File(FILE_TO_BE_READ);
            String response = bot.chat("Read the contents of the file " + file.getAbsolutePath());
            System.out.println("RESPONSE: " + response);
        } finally {
            mcpClient.close();
        }
    }




}
