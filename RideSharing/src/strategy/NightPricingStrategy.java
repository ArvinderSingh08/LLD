package strategy;

import java.time.LocalTime;
import enums.RideProduct;
import factory.RideProductFactory;

public class NightPricingStrategy implements PricingStrategy {

    @Override
    public double calculate(RideProduct product) {
        double base = RideProductFactory.basePrice(product);
        return isNight() ? base * 1.5 : base;
    }

    private boolean isNight() {
        LocalTime now = LocalTime.now();
        return now.isAfter(LocalTime.of(22, 0)) ||
                now.isBefore(LocalTime.of(6, 0));
    }
}

