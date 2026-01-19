package model;

import java.util.concurrent.atomic.AtomicBoolean;
import enums.RideProduct;

public class Driver {

    private final String id;
    private final double rating;
    private final int x;
    private final int y;
    private final Vehicle vehicle;

    private final AtomicBoolean available = new AtomicBoolean(true);

    public Driver(String id, double rating, int x, int y, Vehicle vehicle) {
        this.id = id;
        this.rating = rating;
        this.x = x;
        this.y = y;
        this.vehicle = vehicle;
    }

    public boolean tryAssign() {
        return available.compareAndSet(true, false);
    }

    public boolean isAvailable() {
        return available.get();
    }

    public int distanceFrom(int rx, int ry) {
        return Math.abs(x - rx) + Math.abs(y - ry);
    }

    public RideProduct getProduct() {
        return vehicle.getProduct();
    }

    public String getId() {
        return id;
    }
}
