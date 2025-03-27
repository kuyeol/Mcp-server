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


  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public ApiType getApiType()
  {
    return apiType;
  }

  public void setApiType(ApiType apiType)
  {
    this.apiType = apiType;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public ModelProvider getProvider()
  {
    return provider;
  }

  public void setProvider(ModelProvider provider)
  {
    this.provider = provider;
  }
}
