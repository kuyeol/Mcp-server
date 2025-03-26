package org.acme.panache;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import io.smallrye.mutiny.Uni;
import org.acme.panache.entity.ModelProvider;

@Path("/prices")
public class PriceResource {

    @GET
    public Uni<List<ModelProvider>> getAllPrices() {
        return ModelProvider.listAll();
    }
}
