package org.acme.panache.entity;


import jakarta.persistence.*;
import org.acme.panache.constant.ApiType;

@Entity
public class ApiValue
{
  @Id
  @GeneratedValue
  public String  id;
  public ApiType apiType;
  public String  value;

  @ManyToOne(fetch = FetchType.LAZY)
  public ModelProvider provider;


}
