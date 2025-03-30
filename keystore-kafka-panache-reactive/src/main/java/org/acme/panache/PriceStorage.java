package org.acme.panache;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PriceStorage
{

    static double price;

    @Incoming("prices")
    @ActivateRequestContext
    Uni<Void> store(int priceInUsd) {

        price += ( priceInUsd * priceInUsd );
        System.out.println("Storing price " + price);
        return Uni.createFrom().voidItem();
    }




}
