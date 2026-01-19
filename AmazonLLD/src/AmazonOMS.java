
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* =========================
   Enums
   ========================= */
enum OrderStatus {
    CREATED,
    PAYMENT_AUTHORIZED,
    INVENTORY_RESERVED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

enum ShipmentStatus {
    CREATED,
    DISPATCHED,
    DELIVERED
}

/* =========================
   Product & OrderItem
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

class User {
    final String userId;
    final String name;

    User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}


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
    private static final AtomicInteger ID_GEN = new AtomicInteger(1);

    final int orderId;
    final User user;
    final List<OrderItem> items;
    OrderStatus status;

    Order(User user, List<OrderItem> items) {
        this.orderId = ID_GEN.getAndIncrement();
        this.user = user;
        this.items = items;
        this.status = OrderStatus.CREATED;
    }

    double totalAmount() {
        return items.stream()
                .mapToDouble(i -> i.product.price * i.quantity)
                .sum();
    }
}


/* =========================
   Inventory Service
   ========================= */
class InventoryService {

    // key = warehouseId:sku , value = available qty
    private final ConcurrentHashMap<String, Integer> stock =
            new ConcurrentHashMap<>();

    public void addStock(String warehouseId, int sku, int qty) {
        String key = warehouseId + ":" + sku;
        Integer current = stock.get(key);
        if (current == null) {
            stock.put(key, qty);
        } else {
            stock.put(key, current + qty);
        }
    }

    public boolean reserve(String warehouseId, int sku, int qty) {
        String key = warehouseId + ":" + sku;
        AtomicBoolean success = new AtomicBoolean(false);

        stock.compute(key, (k, available) -> {
            if (available == null || available < qty) {
                return available;
            }
            success.set(true);
            return available - qty;
        });

        return success.get();
    }
}

/* =========================
   Fulfillment Strategy
   ========================= */
interface FulfillmentStrategy {
    String selectWarehouse(int sku);
}

class SimpleFulfillmentStrategy implements FulfillmentStrategy {
    @Override
    public String selectWarehouse(int sku) {
        return "WH-1";
    }
}

/* =========================
   Payment Service
   ========================= */
class PaymentService {

    public boolean authorize(int orderId, double amount) {
        System.out.println("Payment authorized for order " + orderId +
                ", amount = " + amount);
        return true;
    }

    public void capture(int orderId) {
        System.out.println("Payment captured for order " + orderId);
    }

    public void refund(int orderId) {
        System.out.println("Payment refunded for order " + orderId);
    }
}

/* =========================
   Shipment
   ========================= */
class Shipment {
    final int orderId;
    final String warehouseId;
    ShipmentStatus status = ShipmentStatus.CREATED;

    Shipment(int orderId, String warehouseId) {
        this.orderId = orderId;
        this.warehouseId = warehouseId;
    }

    void dispatch() {
        status = ShipmentStatus.DISPATCHED;
        System.out.println("Shipment dispatched from " + warehouseId +
                " for order " + orderId);
    }
}

/* =========================
   Order Service
   ========================= */
class OrderService {

    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final FulfillmentStrategy fulfillmentStrategy;

    OrderService(InventoryService inventoryService,
                 PaymentService paymentService,
                 FulfillmentStrategy fulfillmentStrategy) {
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.fulfillmentStrategy = fulfillmentStrategy;
    }

    public Order placeOrder(User user, List<OrderItem> items) {
        Order order = new Order(user, items);
        // 1. Payment Authorization
        boolean paymentOk = paymentService.authorize(
                order.orderId, order.totalAmount());

        if (!paymentOk) {
            order.status = OrderStatus.CANCELLED;
            return order;
        }
        order.status = OrderStatus.PAYMENT_AUTHORIZED;

        // 2. Inventory Reservation
        for (OrderItem item : items) {
            String warehouse =
                    fulfillmentStrategy.selectWarehouse(item.product.sku);

            boolean reserved = inventoryService.reserve(
                    warehouse, item.product.sku, item.quantity);

            if (!reserved) {
                paymentService.refund(order.orderId);
                order.status = OrderStatus.CANCELLED;
                return order;
            }
        }
        order.status = OrderStatus.INVENTORY_RESERVED;

        // 3. Shipment
        Shipment shipment = new Shipment(order.orderId, "WH-1");
        shipment.dispatch();

        paymentService.capture(order.orderId);
        order.status = OrderStatus.SHIPPED;

        return order;
    }
}

/* =========================
   Main with Demos
   ========================= */
public class AmazonOMS {

    public static void main(String[] args) {

        System.out.println("=== Amazon OMS Demo Start ===\n");

        /* -------------------------------
           Setup Core Services
        -------------------------------- */
        InventoryService inventoryService = new InventoryService();
        PaymentService paymentService = new PaymentService();
        FulfillmentStrategy strategy = new SimpleFulfillmentStrategy();

        OrderService orderService =
                new OrderService(inventoryService, paymentService, strategy);

        /* -------------------------------
           Setup Inventory
        -------------------------------- */
        inventoryService.addStock("WH-1", 101, 10); // Apple
        inventoryService.addStock("WH-1", 102, 5);  // Banana

        Product apple = new Product(101, "Apple", 20);
        Product banana = new Product(102, "Banana", 10);

        /* -------------------------------
           Setup Users
        -------------------------------- */
        User alice = new User("U1", "Alice");
        User bob   = new User("U2", "Bob");

        /* =========================================================
           DEMO 1: Successful Order (Happy Path)
        ========================================================= */
        System.out.println(">>> DEMO 1: Successful Order");

        Order order1 = orderService.placeOrder(
                alice,
                List.of(
                        new OrderItem(apple, 2),
                        new OrderItem(banana, 3)
                )
        );

        System.out.println(
                "OrderId: " + order1.orderId +
                        ", User: " + order1.user.name +
                        ", Status: " + order1.status
        );
        System.out.println();

        /* =========================================================
           DEMO 2: Another Successful Order
        ========================================================= */
        System.out.println(">>> DEMO 2: Another Successful Order");

        Order order2 = orderService.placeOrder(
                bob,
                List.of(
                        new OrderItem(apple, 3)
                )
        );

        System.out.println(
                "OrderId: " + order2.orderId +
                        ", User: " + order2.user.name +
                        ", Status: " + order2.status
        );
        System.out.println();

        /* =========================================================
           DEMO 3: Inventory Failure (Out of Stock)
        ========================================================= */
        System.out.println(">>> DEMO 3: Inventory Failure");

        Order order3 = orderService.placeOrder(
                alice,
                List.of(
                        new OrderItem(apple, 10) // insufficient stock
                )
        );

        System.out.println(
                "OrderId: " + order3.orderId +
                        ", User: " + order3.user.name +
                        ", Status: " + order3.status
        );
        System.out.println();

        /* =========================================================
           DEMO 4: Concurrent Order Placement
        ========================================================= */
        System.out.println(">>> DEMO 4: Concurrent Order Placement");

        InventoryService concurrentInventory = new InventoryService();
        concurrentInventory.addStock("WH-1", 201, 5); // limited stock

        OrderService concurrentOrderService =
                new OrderService(concurrentInventory, paymentService, strategy);

        Product tshirt = new Product(201, "T-Shirt", 500);

        Runnable r1 = () -> {
            User u = new User("U3", "Charlie");
            Order o = concurrentOrderService.placeOrder(
                    u,
                    List.of(new OrderItem(tshirt, 3))
            );
            System.out.println("User " + u.name +
                    " | Order " + o.orderId +
                    " | Status: " + o.status);
        };

        Runnable r2 = () -> {
            User u = new User("U4", "David");
            Order o = concurrentOrderService.placeOrder(
                    u,
                    List.of(new OrderItem(tshirt, 3))
            );
            System.out.println("User " + u.name +
                    " | Order " + o.orderId +
                    " | Status: " + o.status);
        };

        Runnable r3 = () -> {
            User u = new User("U5", "Eva");
            Order o = concurrentOrderService.placeOrder(
                    u,
                    List.of(new OrderItem(tshirt, 1))
            );
            System.out.println("User " + u.name +
                    " | Order " + o.orderId +
                    " | Status: " + o.status);
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException ignored) {}

        System.out.println("\n=== Amazon OMS Demo End ===");
    }
}


