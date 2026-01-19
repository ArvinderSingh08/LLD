package model;

public class Ride {

    private final String id;
    private final String riderId;
    private final String driverId;
    private final double fare;

    public Ride(String id, String riderId, String driverId, double fare) {
        this.id = id;
        this.riderId = riderId;
        this.driverId = driverId;
        this.fare = fare;
    }

    public String getId() {
        return id;
    }
}

