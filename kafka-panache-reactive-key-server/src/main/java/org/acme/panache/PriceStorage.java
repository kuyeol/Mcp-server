package org.acme.panache;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import org.acme.panache.entity.ModelProvider;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;

@ApplicationScoped
public class PriceStorage {

    @Incoming("prices")
    @ActivateRequestContext
    Uni<Void> store(int priceInUsd) {
        ModelProvider price = new ModelProvider();
       // price.value = priceInUsd;
        return Panache.withTransaction(price::persist)
                .replaceWithVoid();

    }

}
