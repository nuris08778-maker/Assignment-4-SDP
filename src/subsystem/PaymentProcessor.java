package subsystem;

import exception.OrderException;
import model.Customer;

import java.util.HashMap;
import java.util.Map;

public class PaymentProcessor {
    private final Map<String, Double> amounts = new HashMap<>();
    private final Map<String, Customer> payers = new HashMap<>();
    private int paymentCounter = 1;

    public String charge(Customer customer, double amount) {
        if (customer.getBalance() < amount) {
            throw new OrderException("Payment declined: not enough money on the card");
        }
        customer.withdraw(amount);
        String paymentId = "PAY-" + paymentCounter++;
        amounts.put(paymentId, amount);
        payers.put(paymentId, customer);
        System.out.println("[Payment] Charged " + amount + " (" + paymentId + ")");
        return paymentId;
    }

    public void refund(String paymentId) {
        Double amount = amounts.remove(paymentId);
        Customer customer = payers.remove(paymentId);
        if (amount == null || customer == null) {
            throw new OrderException("Payment not found: " + paymentId);
        }
        customer.deposit(amount);
        System.out.println("[Payment] Refunded " + amount + " (" + paymentId + ")");
    }
}
