package org.acme.panache;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import org.acme.panache.entity.ModelProvider;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PriceStorage
{

  @Incoming("prices")
  @ActivateRequestContext
  Uni<Void> store(int priceInUsd) {

    ModelProvider price = new ModelProvider();

    return Uni.createFrom().voidItem();
  }

}
