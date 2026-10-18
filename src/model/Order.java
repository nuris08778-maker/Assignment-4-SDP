package model;

public class Order {
    private final String id;
    private final Customer customer;
    private final Cart cart;
    private final String paymentId;
    private final String trackingNumber;
    private OrderStatus status;

    public Order(String id, Customer customer, Cart cart, String paymentId, String trackingNumber) {
        this.id = id;
        this.customer = customer;
        this.cart = cart;
        this.paymentId = paymentId;
        this.trackingNumber = trackingNumber;
        this.status = OrderStatus.PLACED;
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Cart getCart() {
        return cart;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
