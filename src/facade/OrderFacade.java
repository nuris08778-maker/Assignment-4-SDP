package facade;

import exception.OrderException;
import model.Cart;
import model.Customer;
import model.Order;
import model.OrderStatus;
import subsystem.InventoryService;
import subsystem.NotificationService;
import subsystem.PaymentProcessor;
import subsystem.ShippingService;

import java.util.HashMap;
import java.util.Map;

public class OrderFacade {
    private final InventoryService inventoryService = new InventoryService();
    private final PaymentProcessor paymentProcessor = new PaymentProcessor();
    private final ShippingService shippingService = new ShippingService();
    private final NotificationService notificationService = new NotificationService();
    private final Map<String, Order> orders = new HashMap<>();
    private int orderCounter = 1;

    public Order placeOrder(Cart cart, Customer customer) {
        if (cart.isEmpty()) {
            throw new OrderException("Cart is empty");
        }

        inventoryService.reserve(cart);

        String paymentId;
        try {
            paymentId = paymentProcessor.charge(customer, cart.getTotal());
        } catch (OrderException e) {
            inventoryService.release(cart);
            throw e;
        }

        String trackingNumber;
        try {
            trackingNumber = shippingService.createShipment(customer.getAddress());
        } catch (OrderException e) {
            paymentProcessor.refund(paymentId);
            inventoryService.release(cart);
            throw e;
        }

        String orderId = "ORD-" + orderCounter++;
        Order order = new Order(orderId, customer, cart, paymentId, trackingNumber);
        orders.put(orderId, order);

        notificationService.sendEmail(customer.getEmail(), "Your order " + orderId + " is placed");
        return order;
    }

    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderException("Order not found: " + orderId);
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderException("Order already cancelled: " + orderId);
        }

        shippingService.cancelShipment(order.getTrackingNumber());
        paymentProcessor.refund(order.getPaymentId());
        inventoryService.release(order.getCart());
        order.setStatus(OrderStatus.CANCELLED);

        notificationService.sendEmail(order.getCustomer().getEmail(), "Your order " + orderId + " is cancelled");
    }

    public int checkStock(String productId) {
        return inventoryService.getStock(productId);
    }
}
