package model;

import java.time.Instant;
import enums.RideProduct;

public class FareQuote {

    private final RideProduct product;
    private final double amount;
    private final Instant expiresAt;

    public FareQuote(RideProduct product, double amount, Instant expiresAt) {
        this.product = product;
        this.amount = amount;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public RideProduct getProduct() {
        return product;
    }

    public double getAmount() {
        return amount;
    }
}

