package org.acme.panache.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;


@Entity
public class ModelInfo extends PanacheEntity
{

  public String modelName;
  public String providerUrl;
  public String option;


}
