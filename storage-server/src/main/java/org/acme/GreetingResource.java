package org.acme;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import picocli.CommandLine;

@Path("/hello")
@ApplicationScoped
public class GreetingResource
{

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        return "Hello from Quarkus REST";
    }




}
