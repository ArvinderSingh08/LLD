package factory;
import enums.RideProduct;

public class RideProductFactory {
    public static double basePrice(RideProduct product) {
        return switch (product) {
            case BIKE -> 50;
            case AUTO -> 80;
            case SEDAN -> 120;
            case SUV -> 180;
        };
    }
}
