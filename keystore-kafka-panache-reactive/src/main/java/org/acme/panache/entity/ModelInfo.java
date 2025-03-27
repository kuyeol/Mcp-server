package org.acme.panache.entity;

import jakarta.persistence.*;

@Entity
public class ModelInfo
{

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getProviderUrl() {
    return providerUrl;
  }

  public void setProviderUrl(String providerUrl) {
    this.providerUrl = providerUrl;
  }

  public String getOption() {
    return option;
  }

  public void setOption(String option) {
    this.option = option;
  }

  public ModelProvider getProvider() {
    return provider;
  }

  public void setProvider(ModelProvider provider) {
    this.provider = provider;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  public String id;

  public String        modelName;

  public String        providerUrl;

  public String        option;


  @ManyToOne(fetch = FetchType.LAZY)
  public ModelProvider provider;

}
