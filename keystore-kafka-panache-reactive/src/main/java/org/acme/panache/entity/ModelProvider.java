package org.acme.panache.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.acme.panache.constant.ModelType;
import org.acme.panache.constant.ProviderType;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ModelProvider extends PanacheEntity
{

  public ProviderType providerType;

  public ModelType modelType;


  @OneToMany(cascade = CascadeType.ALL)
  public List<ModelInfo> modelInfos = new ArrayList<>();
  @OneToMany(cascade = CascadeType.ALL)
  public List<ApiValue>  apiValues  = new ArrayList<>();


}
