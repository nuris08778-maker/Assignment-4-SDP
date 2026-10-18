import exception.OrderException;
import facade.OrderFacade;
import model.Cart;
import model.Customer;
import model.Order;
import model.Product;

public class Main {

    public static void main(String[] args) {
        OrderFacade shop = new OrderFacade();

        Product laptop = new Product("P1", "Laptop", 1000);
        Product mouse = new Product("P2", "Mouse", 50);
        Product monitor = new Product("P3", "Monitor", 300);

        Customer nursultan = new Customer("Nursultan", "nursultan@mail.com", "Astana, Kabanbay Batyr 53", 2000);

        System.out.println("=== Test 1: successful order ===");
        Cart cart1 = new Cart();
        cart1.addItem(laptop, 1);
        cart1.addItem(mouse, 2);
        Order order1 = shop.placeOrder(cart1, nursultan);
        System.out.println("Order " + order1.getId() + " status: " + order1.getStatus());
        System.out.println("Balance: " + nursultan.getBalance());
        System.out.println("Laptop stock: " + shop.checkStock("P1"));

        System.out.println();
        System.out.println("=== Test 2: payment declined (stock must be released) ===");
        Cart cart2 = new Cart();
        cart2.addItem(monitor, 2);
        cart2.addItem(laptop, 1);
        System.out.println("Monitor stock before: " + shop.checkStock("P3"));
        try {
            shop.placeOrder(cart2, nursultan);
        } catch (OrderException e) {
            System.out.println("Order failed: " + e.getMessage());
        }
        System.out.println("Monitor stock after: " + shop.checkStock("P3"));

        System.out.println();
        System.out.println("=== Test 3: not enough stock ===");
        Cart cart3 = new Cart();
        cart3.addItem(monitor, 5);
        try {
            shop.placeOrder(cart3, nursultan);
        } catch (OrderException e) {
            System.out.println("Order failed: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== Test 4: shipping fails (payment refunded, stock released) ===");
        Customer guest = new Customer("Guest", "guest@mail.com", "", 500);
        Cart cart4 = new Cart();
        cart4.addItem(mouse, 1);
        System.out.println("Guest balance before: " + guest.getBalance());
        try {
            shop.placeOrder(cart4, guest);
        } catch (OrderException e) {
            System.out.println("Order failed: " + e.getMessage());
        }
        System.out.println("Guest balance after: " + guest.getBalance());
        System.out.println("Mouse stock: " + shop.checkStock("P2"));

        System.out.println();
        System.out.println("=== Test 5: cancel order ===");
        shop.cancelOrder(order1.getId());
        System.out.println("Order " + order1.getId() + " status: " + order1.getStatus());
        System.out.println("Balance: " + nursultan.getBalance());
        System.out.println("Laptop stock: " + shop.checkStock("P1"));

        System.out.println();
        System.out.println("=== Test 6: cancel the same order again ===");
        try {
            shop.cancelOrder(order1.getId());
        } catch (OrderException e) {
            System.out.println("Cancel failed: " + e.getMessage());
        }
    }
}
