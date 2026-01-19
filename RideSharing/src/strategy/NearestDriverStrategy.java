package strategy;

import model.Driver;

import java.util.List;

public class NearestDriverStrategy implements DriverMatchingStrategy {

    @Override
    public Driver find(List<Driver> drivers, int x, int y) {
        Driver best = null;
        int minDist = Integer.MAX_VALUE;

        for (Driver d : drivers) {
            if (!d.isAvailable()) continue;

            int dist = d.distanceFrom(x, y);
            if (dist < minDist) {
                minDist = dist;
                best = d;
            }
        }
        return best;
    }
}

