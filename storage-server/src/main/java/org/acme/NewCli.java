package org.acme;

/**
 * This class provides methods for creating files and executing commands in different operating systems.
 * It includes functionality to create a text file, execute commands in both Unix-like and Windows environments,
 * and handle potential exceptions during file operations and command execution.
 */
import java.io.*;
import java.time.Instant;

public class NewCli
{

    public static String exeCli = "dir";
    public static void main(String[] args) {

        String fileName = "text" + Instant.now().getEpochSecond()+".java";
        createFile(fileName,content);
        execute2Command(exeCli, exeCli);
    }
   static   String content ="import dev.langchain4j.code.judge0.Judge0JavaScriptExecutionTool;\n" +
                            "import dev.langchain4j.memory.chat.MessageWindowChatMemory;\n" +
                            "import dev.langchain4j.model.chat.ChatLanguageModel;\n" +
                            "import dev.langchain4j.model.openai.OpenAiChatModel;\n" +
                            "import dev.langchain4j.service.AiServices;\n" + "\n" +
                            "import static java.time.Duration.ofSeconds;\n" + "\n" +
                            "public class ServiceWithDynamicToolsExample {\n" + "\n" + "    interface Assistant {\n" +
                            "\n" + "        String chat(String message);\n" + "    }\n" + "\n" +
                            "    public static void main(String[] args) {\n" + "\n" +
                            "        Judge0JavaScriptExecutionTool judge0Tool = new Judge0JavaScriptExecutionTool(ApiKeys.RAPID_API_KEY);\n" +
                            "\n" + "        ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()\n" +
                            "                .apiKey(ApiKeys.OPENAI_API_KEY)\n" +
                            "                .temperature(0.0)\n" + "                .timeout(ofSeconds(60))\n" +
                            "                .build();\n" + "\n" +
                            "        Assistant assistant = AiServices.builder(Assistant.class)\n" +
                            "                .chatLanguageModel(chatLanguageModel)\n" +
                            "                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))\n" +
                            "                .tools(judge0Tool)\n" + "                .build();\n" + "\n" +
                            "        interact(assistant, \"What is the square root of 49506838032859?\");\n" +
                            "        interact(assistant, \"Capitalize every third letter: abcabc\");\n" +
                            "        interact(assistant, \"What is the number of hours between 17:00 on 21 Feb 1988 and 04:00 on 12 Apr 2014?\");\n" +
                            "    }\n" + "\n" +
                            "    private static void interact(Assistant assistant, String userMessage) {\n" +
                            "        System.out.println(\"[User]: \" + userMessage);\n" +
                            "        String answer = assistant.chat(userMessage);\n" +
                            "        System.out.println(\"[Assistant]: \" + answer);\n" +
                            "        System.out.println();\n" + "        System.out.println();\n" + "    }\n" + "}";

    /**
     *
     * 파일 생성
     * 파일 경로 : 파일명만 사용 시 현재 프로젝트 루트, 경로 추가 시 절대 경로
     * ....
     * Creates a file
     * It also reads and prints the content of the file to the console.
     */

    public static void createFile(String FileName,String content) {

        String filePath = FileName; // File path (relative or absolute)

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("File created and written successfully!");
        } catch (IOException e) {
            System.err.println("Error creating/writing file: " + e.getMessage());
        }

        // Optional: Read the file to verify content
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            System.out.println("File Content:");
            String line;
            while (( line = reader.readLine() ) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Executes a command in a Unix-like environment using bash.
     * @param command The command to be executed.
     */
    public static void executeCommand(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);

        try {
            Process        process = processBuilder.start();
            BufferedReader reader  = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String         line;
            while (( line = reader.readLine() ) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a command based on the operating system.
     * If the OS is Windows, it executes the windowsCommand using cmd.exe.
     * Otherwise, it executes the unixCommand using bash.
     * @param unixCommand The command to be executed in Unix-like environments.
     * @param windowsCommand The command to be executed in Windows environments. */
    public static void execute2Command(String unixCommand, String windowsCommand) {
        ProcessBuilder processBuilder;
        String         os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            processBuilder = new ProcessBuilder("cmd.exe", "/c", windowsCommand);
        } else {
            processBuilder = new ProcessBuilder("bash", "-c", unixCommand);
        }
        try {
            Process        process = processBuilder.start();
            BufferedReader reader  = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String         line;
            while (( line = reader.readLine() ) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }




}
