import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* =========================
   Product & Factory
   ========================= */

class Product {
    final int sku;
    final String name;
    final double price;

    Product(int sku, String name, double price) {
        this.sku = sku;
        this.name = name;
        this.price = price;
    }
}

class ProductFactory {
    public static Product getProduct(int sku) {
        return switch (sku) {
            case 101 -> new Product(101, "Apple", 20);
            case 102 -> new Product(102, "Banana", 10);
            case 103 -> new Product(103, "Chocolate", 50);
            default  -> new Product(sku, "Item-" + sku, 100);
        };
    }
}

/* =========================
   Inventory (Thread-safe)
   ========================= */

class Inventory {

    private final ConcurrentHashMap<Integer, Integer> stock =
            new ConcurrentHashMap<>();

//    public void addStock(int sku, int qty) {
//        Integer current = stock.get(sku);
//        if (current == null) {
//            stock.put(sku, qty);
//        } else {
//            stock.put(sku, current + qty);
//        }
//    }

    public void addStock(int sku, int qty) {
        stock.compute(sku, (k, current) -> current == null ? qty : current + qty);
    }


    public boolean reserve(int sku, int qty) {
        AtomicBoolean success = new AtomicBoolean(false);

        stock.compute(sku, (k, available) -> {
            if (available == null || available < qty) {
                return available;
            }
            success.set(true);
            return available - qty;
        });

        return success.get();
    }

    public int getAvailable(int sku) {
        Integer available = stock.get(sku);
        return available == null ? 0 : available;
    }
}

/* =========================
   Dark Store
   ========================= */

class DarkStore {
    final String id;
    final double x, y;
    final Inventory inventory = new Inventory();

    DarkStore(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    void addStock(int sku, int qty) {
        inventory.addStock(sku, qty);
    }

    boolean reserveItem(int sku, int qty) {
        return inventory.reserve(sku, qty);
    }

    int checkStock(int sku) {
        return inventory.getAvailable(sku);
    }
}

/* =========================
   User
   ========================= */

class User {
    final String name;
    final double x, y;

    User(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
}

/* =========================
   Store Selection Strategy
   ========================= */

interface StoreSelectionStrategy {
    DarkStore selectStore(List<DarkStore> stores, User user);
}

class NearestStoreStrategy implements StoreSelectionStrategy {
    @Override
    public DarkStore selectStore(List<DarkStore> stores, User user) {
        return stores.stream()
                .min(Comparator.comparing(
                        s -> Math.hypot(s.x - user.x, s.y - user.y)))
                .orElseThrow(() -> new RuntimeException("No store available"));
    }
}

/* =========================
   Order Item
   ========================= */

class OrderItem {
    final Product product;
    final int quantity;

    OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}

/* =========================
   Order
   ========================= */

class Order {
    static AtomicInteger ID_GEN = new AtomicInteger(1);

    final int orderId;
    final User user;
    final List<OrderItem> items;

    Order(User user, List<OrderItem> items) {
        this.orderId = ID_GEN.getAndIncrement();
        this.user = user;
        this.items = items;
    }

    double totalAmount() {
        double sum = 0;
        for (OrderItem item : items) {
            sum += item.product.price * item.quantity;
        }
        return sum;
    }
}


/* =========================
   Order Service (Singleton)
   ========================= */

class OrderService {

    private static final OrderService INSTANCE = new OrderService();
    private final StoreSelectionStrategy storeStrategy = new NearestStoreStrategy();

    private OrderService() {}

    public static OrderService getInstance() {
        return INSTANCE;
    }

    public Order placeOrder(
            User user,
            Map<Integer, Integer> skuToQty,
            List<DarkStore> stores) {

        DarkStore store = storeStrategy.selectStore(stores, user);

        // 1. Reserve inventory
        for (var entry : skuToQty.entrySet()) {
            boolean reserved = store.reserveItem(entry.getKey(), entry.getValue());
            if (!reserved) {
                throw new RuntimeException(
                        "Out of stock for SKU " + entry.getKey());
            }
        }

        // 2. Build order using Product
        List<OrderItem> items = new ArrayList<>();
        for (var entry : skuToQty.entrySet()) {
            Product product = ProductFactory.getProduct(entry.getKey());
            items.add(new OrderItem(product, entry.getValue()));
        }

        Order order = new Order(user, items);

        System.out.println(
                "Order placed successfully. OrderId = " + order.orderId +
                        ", Total = ₹" + order.totalAmount());

        return order;
    }
}

/* =========================
   Main (Demo + Concurrency)
   ========================= */

public class ZeptoOMS {

    public static void main(String[] args) throws InterruptedException {

        DarkStore store = new DarkStore("DS-1", 0, 0);
        store.addStock(101, 10); // Apple
        store.addStock(102, 5);  // Banana

        List<DarkStore> stores = List.of(store);

        User u1 = new User("Amit", 1, 1);
        User u2 = new User("Rohit", 2, 2);

        Map<Integer, Integer> order1 = Map.of(101, 6);
        Map<Integer, Integer> order2 = Map.of(101, 5);

        Runnable r1 = () -> {
            try {
                OrderService.getInstance().placeOrder(u1, order1, stores);
            } catch (Exception e) {
                System.out.println("Order1 failed: " + e.getMessage());
            }
        };

        Runnable r2 = () -> {
            try {
                OrderService.getInstance().placeOrder(u2, order2, stores);
            } catch (Exception e) {
                System.out.println("Order2 failed: " + e.getMessage());
            }
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(
                "Final stock for SKU 101 = " +
                        store.inventory.getAvailable(101));
    }
}