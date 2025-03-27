package org.acme.panache.dto;

import org.acme.panache.constant.ModelType;
import org.acme.panache.constant.ProviderType;
import org.acme.panache.entity.ApiValue;
import org.acme.panache.entity.ModelInfo;

import java.util.List;

public record ApiRecord(String id,
                        ProviderType providerType,
                        ModelType modelType,
                        List<String> modelInfos,
                        List<String> apiValues)
{


  public ApiRecord(String id,
                   ProviderType providerType,
                   ModelType modelType,
                   List<String> modelInfos,
                   List<String> apiValues)
  {
    this.id           = id;
    this.providerType = providerType;
    this.modelType    = modelType;
    this.modelInfos   = modelInfos;
    this.apiValues    = apiValues;
  }

}