package org.acme.panache.entity;


import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import org.acme.panache.constant.ApiType;

@Entity
public class ApiValue extends PanacheEntity
{
  public ApiType apiType;
  public String  value;


}
