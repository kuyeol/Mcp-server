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
