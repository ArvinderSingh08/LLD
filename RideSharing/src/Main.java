import enums.RideProduct;
import model.*;
import service.DriverMatchingService;
import service.PricingService;
import service.RideService;
import store.InMemoryStore;
import strategy.NearestDriverStrategy;
import strategy.NightPricingStrategy;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        InMemoryStore store = InMemoryStore.getInstance();

        // ============================
        // SETUP COMMON DATA
        // ============================
        Rider r1 = new Rider("R1");
        Rider r2 = new Rider("R2");
        Rider r3 = new Rider("R3");

        Vehicle bike = new Vehicle(RideProduct.BIKE);
        Vehicle sedan = new Vehicle(RideProduct.SEDAN);

        Driver d1 = new Driver("D1", 4.8, 10, 10, bike);
        Driver d2 = new Driver("D2", 4.5, 2, 2, sedan);

        store.riders.put(r1.getId(), r1);
        store.riders.put(r2.getId(), r2);
        store.riders.put(r3.getId(), r3);

        store.drivers.put(d1.getId(), d1);
        store.drivers.put(d2.getId(), d2);

        PricingService pricingService =
                PricingService.getInstance(new NightPricingStrategy());

        DriverMatchingService matchingService =
                DriverMatchingService.getInstance(new NearestDriverStrategy());

        RideService rideService =
                RideService.getInstance(matchingService, pricingService);

        // ============================
        // DEMO 1: NORMAL BOOKING (BIKE)
        // ============================
        System.out.println("\n--- DEMO 1: Normal Booking (BIKE) ---");
        try {
            Ride ride1 = rideService.bookRide("R1", RideProduct.BIKE, 0, 0);
            System.out.println("Ride booked successfully: " + ride1.getId());
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        // ============================
        // DEMO 2: CONCURRENT BOOKING (SEDAN)
        // Only ONE rider should get the sedan driver
        // ============================
        System.out.println("\n--- DEMO 2: Concurrent Booking (SEDAN) ---");

        Runnable task1 = () -> {
            try {
                Ride ride = rideService.bookRide("R2", RideProduct.SEDAN, 0, 0);
                System.out.println("R2 booked: " + ride.getId());
            } catch (Exception e) {
                System.out.println("R2 failed: " + e.getMessage());
            }
        };

        Runnable task2 = () -> {
            try {
                Ride ride = rideService.bookRide("R3", RideProduct.SEDAN, 0, 0);
                System.out.println("R3 booked: " + ride.getId());
            } catch (Exception e) {
                System.out.println("R3 failed: " + e.getMessage());
            }
        };

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // ============================
        // DEMO 3: REQUEST PRODUCT WITH NO DRIVER
        // AUTO is not supported by any driver
        // ============================
        System.out.println("\n--- DEMO 3: No Driver For Requested Product (AUTO) ---");
        try {
            rideService.bookRide("R1", RideProduct.AUTO, 0, 0);
        } catch (Exception e) {
            System.out.println("Expected failure: " + e.getMessage());
        }

        // ============================
        // DEMO 4: ALL DRIVERS BUSY
        // BIKE driver already used in Demo 1
        // ============================
        System.out.println("\n--- DEMO 4: All Drivers Busy (BIKE) ---");
        try {
            rideService.bookRide("R2", RideProduct.BIKE, 5, 5);
        } catch (Exception e) {
            System.out.println("Expected failure: " + e.getMessage());
        }

        // ============================
        // DEMO 5: PRICING BY VEHICLE TYPE
        // Shows pricing logic is product-based
        // ============================
        System.out.println("\n--- DEMO 5: Product Pricing ---");
        System.out.println("BIKE fare  : " +
                pricingService.getFare(RideProduct.BIKE).getAmount());
        System.out.println("SEDAN fare : " +
                pricingService.getFare(RideProduct.SEDAN).getAmount());
        System.out.println("AUTO fare  : " +
                pricingService.getFare(RideProduct.AUTO).getAmount());
        System.out.println("SUV fare   : " +
                pricingService.getFare(RideProduct.SUV).getAmount());
    }
}
