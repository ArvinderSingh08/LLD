package strategy;

import enums.RideProduct;

public interface PricingStrategy {
    double calculate(RideProduct product);
}

