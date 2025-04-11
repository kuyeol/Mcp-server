package org.acme.agent;

public enum GoogleModels
{
  GEMINI2_PRO("gemini-2.5-pro-exp-03-25"),
  GEMINI2_FLASH("gemini-2.0-flash-lite"),
  GEMINI2_IMG("gemini-2.0-flash-exp-image-generation"),

  GEMMA3_27B("gemma-3-27b-it"),
  ;

  private final String modelCode;


  GoogleModels(String modelCode)
  {
    this.modelCode = modelCode;
  }


  public String getModelCode()
  {
    return modelCode;
  }
}
