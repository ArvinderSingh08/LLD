package service;
import model.Driver;
import model.FareQuote;
import model.Ride;
import store.InMemoryStore;

import java.util.List;
import java.util.UUID;

import enums.RideProduct;

public class RideService {

    private static RideService INSTANCE;
    private final DriverMatchingService matchingService;
    private final PricingService pricingService;
    private final InMemoryStore store = InMemoryStore.getInstance();

    private RideService(DriverMatchingService ms, PricingService ps) {
        this.matchingService = ms;
        this.pricingService = ps;
    }

    public static synchronized RideService getInstance(
            DriverMatchingService ms,
            PricingService ps) {
        if (INSTANCE == null) {
            INSTANCE = new RideService(ms, ps);
        }
        return INSTANCE;
    }

    public Ride bookRide(
            String riderId,
            RideProduct requestedProduct,
            int x,
            int y
    ) {

        // 1️⃣ Filter drivers by requested product
        List<Driver> eligibleDrivers = store.drivers.values()
                .stream()
                .filter(d -> d.isAvailable())
                .filter(d -> d.getProduct() == requestedProduct)
                .toList();

        if (eligibleDrivers.isEmpty()) {
            throw new RuntimeException("No drivers for requested product");
        }

        // 2️⃣ Apply matching strategy
        Driver driver = matchingService.findDriver(eligibleDrivers, x, y);

        if (driver == null || !driver.tryAssign()) {
            throw new RuntimeException("Driver already assigned");
        }

        // 3️⃣ Fare derived from driver's vehicle
        FareQuote fare = pricingService.getFare(driver.getProduct());

        if (fare.isExpired()) {
            throw new RuntimeException("Fare expired");
        }

        Ride ride = new Ride(
                UUID.randomUUID().toString(),
                riderId,
                driver.getId(),
                fare.getAmount()
        );

        store.rides.put(ride.getId(), ride);
        return ride;
    }
}
