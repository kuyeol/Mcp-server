package org.acme.panache;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Random;

/**
 * A bean producing random prices every 5 seconds.
 * The prices are written to a Kafka topic (prices). The Kafka configuration is specified in the application configuration.
 */
public class PriceGenerator
{

  private Random random = new Random();


  Multi<Integer> generate()
  {
    return Multi.createFrom()
                .ticks()
                .every(Duration.ofSeconds(11L))
                .map(tick -> random.nextInt(111));
  }

}
