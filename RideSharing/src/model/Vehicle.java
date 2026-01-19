package model;

import enums.RideProduct;

public class Vehicle {

    private final RideProduct product;

    public Vehicle(RideProduct product) {
        this.product = product;
    }

    public RideProduct getProduct() {
        return product;
    }
}

