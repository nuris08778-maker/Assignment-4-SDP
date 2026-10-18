package subsystem;

import exception.OrderException;
import model.Cart;
import model.CartItem;

import java.util.HashMap;
import java.util.Map;

public class InventoryService {
    private final Map<String, Integer> stock = new HashMap<>();

    public InventoryService() {
        stock.put("P1", 10);
        stock.put("P2", 5);
        stock.put("P3", 2);
    }

    public int getStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    public void reserve(Cart cart) {
        for (CartItem item : cart.getItems()) {
            int available = getStock(item.getProduct().getId());
            if (available < item.getQuantity()) {
                throw new OrderException("Not enough stock for " + item.getProduct().getName());
            }
        }
        for (CartItem item : cart.getItems()) {
            String id = item.getProduct().getId();
            stock.put(id, getStock(id) - item.getQuantity());
        }
        System.out.println("[Inventory] Items reserved");
    }

    public void release(Cart cart) {
        for (CartItem item : cart.getItems()) {
            String id = item.getProduct().getId();
            stock.put(id, getStock(id) + item.getQuantity());
        }
        System.out.println("[Inventory] Items released back to stock");
    }
}
