package service;
import model.Driver;
import strategy.DriverMatchingStrategy;

import java.util.List;

public class DriverMatchingService {

    private static DriverMatchingService INSTANCE;
    private final DriverMatchingStrategy strategy;

    private DriverMatchingService(DriverMatchingStrategy strategy) {
        this.strategy = strategy;
    }

    public static synchronized DriverMatchingService getInstance(
            DriverMatchingStrategy strategy) {
        if (INSTANCE == null) {
            INSTANCE = new DriverMatchingService(strategy);
        }
        return INSTANCE;
    }

    public Driver findDriver(List<Driver> drivers, int x, int y) {
        return strategy.find(drivers, x, y);
    }
}

