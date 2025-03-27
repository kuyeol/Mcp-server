package org.acme.panache.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.panache.constant.ProviderType;
import org.acme.panache.entity.ModelInfo;
import org.acme.panache.entity.ModelProvider;

@ApplicationScoped
public class ProviderDAO
{
  private static EntityManager em;

  private boolean existsProvider = false;

  public ProviderDAO(EntityManager em)
  {

    ProviderDAO.em = em;
  }

  @Transactional
  public boolean exitsProvider(String id)
  {
    if (em.find(ModelProvider.class,
                id) != null) {
      this.existsProvider = true;
      return existsProvider;
    } else {
      return existsProvider;
    }
  }

  public ModelProvider insertProvider()
  {

    ModelProvider provider = new ModelProvider();
    provider.setProviderType(ProviderType.OLLAMA);
    em.persist(provider);

    return em.getReference(ModelProvider.class,
                           provider.id);
  }

  @Transactional
  public ModelProvider addProvider(String id)
  {
    if (!exitsProvider(id)) {
      ModelProvider provider = insertProvider();

      ModelInfo modelInfo = new ModelInfo();
      modelInfo.setModelName("asdf");
      provider.modelInfos.add(modelInfo);
      em.persist(provider);

      em.flush();
      return provider;

    } else {
      System.out.println(exitsProvider(id));
      return em.find(ModelProvider.class,
                     id);
    }
  }





  // new Entity
  // find Entity
  // updata Entity
  // delete Entity




}
