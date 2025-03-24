package org.acme.agent;

import dev.langchain4j.model.github.GitHubModelsChatModel;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class GithubProvider
{


  static GitHubModelsChatModel GPT4o;

  public GithubProvider(final String token) {
    this.GPT4o = GitHubModelsChatModel.builder().gitHubToken(token).modelName("gpt-4o-mini").build();
  }

  public GitHubModelsChatModel getGPT4o() {
    return this.GPT4o;
  }

}
