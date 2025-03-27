package org.acme.panache.entity;

import jakarta.persistence.*;
import org.acme.panache.constant.ModelType;
import org.acme.panache.constant.ProviderType;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;


@Entity
public class ModelProvider
{
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ProviderType getProviderType() {
    return providerType;
  }

  public void setProviderType(ProviderType providerType) {
    this.providerType = providerType;
  }

  public ModelType getModelType() {
    return modelType;
  }

  public void setModelType(ModelType modelType) {
    this.modelType = modelType;
  }

  public List<ModelInfo> getModelInfos() {
    return modelInfos;
  }

  public void setModelInfos(List<ModelInfo> modelInfos) {
    this.modelInfos = modelInfos;
  }

  public List<ApiValue> getApiValues() {
    return apiValues;
  }

  public void setApiValues(List<ApiValue> apiValues) {
    this.apiValues = apiValues;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  public String id;

  public ProviderType providerType;

  public ModelType modelType;


  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "provider")
  @BatchSize(size = 20)
  public List<ModelInfo> modelInfos = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "provider")
  @BatchSize(size = 20)
  public List<ApiValue> apiValues = new ArrayList<>();


}
