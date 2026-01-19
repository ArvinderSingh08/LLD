package store;

import model.Driver;
import model.Ride;
import model.Rider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStore {

    private static final InMemoryStore INSTANCE = new InMemoryStore();

    public final Map<String, Driver> drivers = new ConcurrentHashMap<>();
    public final Map<String, Rider> riders = new ConcurrentHashMap<>();
    public final Map<String, Ride> rides = new ConcurrentHashMap<>();

    private InMemoryStore() {}

    public static InMemoryStore getInstance() {
        return INSTANCE;
    }
}

