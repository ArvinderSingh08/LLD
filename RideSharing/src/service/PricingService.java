package service;

import enums.RideProduct;
import model.FareQuote;
import strategy.PricingStrategy;

import java.time.Instant;

public class PricingService {

    private static PricingService INSTANCE;
    private static final int TTL_SECONDS = 5;

    private final PricingStrategy strategy;

    private PricingService(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public static synchronized PricingService getInstance(PricingStrategy strategy) {
        if (INSTANCE == null) {
            INSTANCE = new PricingService(strategy);
        }
        return INSTANCE;
    }

    public FareQuote getFare(RideProduct product) {
        double price = strategy.calculate(product);
        return new FareQuote(
                product,
                price,
                Instant.now().plusSeconds(TTL_SECONDS)
        );
    }
}

