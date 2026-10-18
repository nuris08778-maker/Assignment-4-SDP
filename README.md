# Online Order Processing Facade

**Student:** Nursultan Nurislam, SE - 2538
**Course:** ShP-2216 - Software Design Patterns
**Assignment #4:** Facade Pattern (Option B - Online Order Processing Facade)

## About

This project shows the Facade design pattern in Java. An online shop has four subsystem services that must be used in the right order to process an order. The `OrderFacade` class hides this and gives the client two simple methods:

- `placeOrder(cart, customer)`
- `cancelOrder(orderId)`

If one of the steps fails, the facade rolls back the previous steps. For example, if the payment is declined, the reserved stock is released.

## Project structure

```
order-facade
├── README.md
├── .gitignore
└── src
    ├── Main.java
    ├── exception
    │   └── OrderException.java
    ├── facade
    │   └── OrderFacade.java
    ├── model
    │   ├── Cart.java
    │   ├── CartItem.java
    │   ├── Customer.java
    │   ├── Order.java
    │   ├── OrderStatus.java
    │   └── Product.java
    └── subsystem
        ├── InventoryService.java
        ├── NotificationService.java
        ├── PaymentProcessor.java
        └── ShippingService.java
```

## Classes

| Class | Responsibility |
|-------|----------------|
| `InventoryService` | Checks stock, reserves items and releases them back |
| `PaymentProcessor` | Charges the customer and refunds the payment |
| `ShippingService` | Creates and cancels a shipment |
| `NotificationService` | Sends email messages to the customer |
| `OrderFacade` | Calls the services in the correct order and does the rollback |
| `Main` | Client, uses only the facade |

## How the facade works

`placeOrder`:
1. Reserve stock (`InventoryService`)
2. Charge the customer (`PaymentProcessor`). If this fails, release the stock.
3. Create a shipment (`ShippingService`). If this fails, refund the payment and release the stock.
4. Send a confirmation email (`NotificationService`)

`cancelOrder`:
1. Cancel the shipment
2. Refund the payment
3. Release the stock
4. Send a cancellation email

## How to run

Requirements: JDK 17.

From the project root folder:

```
mkdir out
javac -d out $(find src -name "*.java")
java -cp out Main
```

On Windows PowerShell:

```
mkdir out
javac -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
java -cp out Main
```

You can also open the folder in IntelliJ IDEA, mark `src` as Sources Root and run `Main`.

## What the demo shows

1. Successful order
2. Payment declined, stock is released
3. Not enough stock
4. Shipping fails, payment is refunded and stock is released
5. Cancel an order
6. Cancel the same order again (error)

## Notes about the pattern

- The client (`Main`) works only with `OrderFacade` and the model classes.
- Subsystem classes do not know about the facade, so they can still be used directly.
