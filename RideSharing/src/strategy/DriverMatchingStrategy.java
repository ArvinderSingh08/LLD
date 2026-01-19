package strategy;

import model.Driver;

import java.util.List;

public interface DriverMatchingStrategy {
    Driver find(List<Driver> drivers, int x, int y);
}

