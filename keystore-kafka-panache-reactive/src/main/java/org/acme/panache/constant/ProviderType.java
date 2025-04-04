package org.acme.panache.constant;

public enum ProviderType
{
  GOOGLE("google"),
  JLAMA("jlama"),
  OLLAMA("ollama"),
  ;


  private final String providerName;

  ProviderType(String providerName) {
    this.providerName = providerName;
  }
}
