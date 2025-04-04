package org.acme.panache.oldentity;

import jakarta.persistence.*;
import org.acme.panache.constant.ModelType;
import org.acme.panache.constant.ProviderType;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;


@Entity
public class ModelProvider
{
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  public String id;

  public ProviderType providerType;

  public ModelType modelType;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "provider")
  @BatchSize(size = 20)
  public List<ModelInfo> modelParameters = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "provider")
  @BatchSize(size = 20)
  public List<ApiValue> apiValues = new ArrayList<>();


  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public ProviderType getProviderType()
  {
    return providerType;
  }

  public void setProviderType(ProviderType providerType)
  {
    this.providerType = providerType;
  }

  public ModelType getModelType()
  {
    return modelType;
  }

  public void setModelType(ModelType modelType)
  {
    this.modelType = modelType;
  }

  public List<ModelInfo> getModelParameters()
  {
    return modelParameters;
  }

  public List<ApiValue> getApiValues()
  {
    return apiValues;
  }

  public void addAttribute(ApiValue apiValue)
  {
    this.apiValues.add(apiValue);
  }

  public void addAttribute(ModelInfo modelInfo) {
    this.modelParameters.add(modelInfo);
  }


}
