package org.acme.agent;

import dev.langchain4j.model.github.GitHubModelsChatModel;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class GithubProvider
{
  private final static String GIT_TOKEN
    = "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";
  static GitHubModelsChatModel GPT4o;

  public GithubProvider() {
    this.GPT4o = GitHubModelsChatModel.builder().gitHubToken(GIT_TOKEN).modelName("gpt-4o-mini").build();
  }

  public GitHubModelsChatModel getGPT4o() {
    return this.GPT4o;
  }

}
