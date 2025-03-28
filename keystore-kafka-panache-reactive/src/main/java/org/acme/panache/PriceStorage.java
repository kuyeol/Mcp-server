package org.acme.panache;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PriceStorage
{

 // @Incoming("prices")
  @ActivateRequestContext
  Uni<Void> store(int priceInUsd) {


    return Uni.createFrom().voidItem();
  }

}
