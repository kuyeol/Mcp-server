package org.acme.panache;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import io.smallrye.mutiny.Uni;
import org.acme.panache.constant.ModelType;
import org.acme.panache.constant.ProviderType;
import org.acme.panache.entity.ModelProvider;
import org.jboss.logging.annotations.Pos;

@Path("/prices")
public class PriceResource {

    @GET
    public Uni<List<ModelProvider>> getAllPrices() {
        return ModelProvider.listAll();
    }



    @POST
    public Uni<ModelProvider> createPrice(ModelProvider price) {
        ModelProvider provider = new ModelProvider();

        provider.apiValues=price.apiValues;
        provider.modelType=ModelType.LLM;
        provider.providerType=ProviderType.OLLAMA;
        provider.modelInfos=price.modelInfos;


        return provider.persistAndFlush();
    }

}
