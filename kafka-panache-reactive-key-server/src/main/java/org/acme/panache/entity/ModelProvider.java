package org.acme.panache.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import org.acme.panache.constant.ModelType;
import org.acme.panache.constant.ProviderType;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ModelProvider extends PanacheEntity
{

  public ProviderType providerType;
  public ModelType    modelType;

  public List<ModelInfo> modelInfos = new ArrayList<>();
  public List<ApiValue>  apiValues  = new ArrayList<>();


}
