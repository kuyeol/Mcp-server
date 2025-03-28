package org.acme.panache.constant;

public enum ModelType
{
  LLM("llm"),
  EMBEDDED("embedded");

  private final String modelType;

  ModelType(String modelType) {
    this.modelType = modelType;
  }
}
