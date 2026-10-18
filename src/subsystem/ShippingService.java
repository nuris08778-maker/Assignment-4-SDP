package subsystem;

import exception.OrderException;

import java.util.HashSet;
import java.util.Set;

public class ShippingService {
    private final Set<String> activeShipments = new HashSet<>();
    private int shipmentCounter = 1;

    public String createShipment(String address) {
        if (address == null || address.isBlank()) {
            throw new OrderException("Shipping failed: address is empty");
        }
        String trackingNumber = "TRK-" + shipmentCounter++;
        activeShipments.add(trackingNumber);
        System.out.println("[Shipping] Shipment " + trackingNumber + " created for " + address);
        return trackingNumber;
    }

    public void cancelShipment(String trackingNumber) {
        if (!activeShipments.remove(trackingNumber)) {
            throw new OrderException("Shipment not found: " + trackingNumber);
        }
        System.out.println("[Shipping] Shipment " + trackingNumber + " cancelled");
    }
}
