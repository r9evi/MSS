package order;

import callback.Status;
import currency.Currency;

public class Order {
    private static int idCounter = 0;

    private final double initialQuantity;

    private final int clientId;
    private int orderId;
    private final Currency baseCurrency;
    private final Currency quoteCurrency;
    private final OrderType orderType;
    private final double price;
    private double quantity;
    private Status status;

    public Order(int clientId, Currency baseCurrency, Currency quoteCurrency, OrderType orderType, double quantity, double price) {
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
        this.status = Status.CREATED;
        this.clientId= clientId;
        this.orderId = idCounter++;
        this.initialQuantity = quantity;
    }

    public double getInitialQuantity() {
        return initialQuantity;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public Currency getQuoteCurrency() {
        return quoteCurrency;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getClientId() {
        return clientId;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("%s %s/%s: %.2f @ %.2f", orderType, baseCurrency, quoteCurrency, quantity, price);
    }
}
