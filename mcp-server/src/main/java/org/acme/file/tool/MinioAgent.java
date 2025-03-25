package org.acme.file.tool;

import dev.langchain4j.service.UserMessage;

public interface MinioAgent
{
    @UserMessage("{{query}}")
    String chat(String query);
}
