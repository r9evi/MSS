package order;

import callback.Callback;
import callback.Status;
import currency.Currency;

import java.util.concurrent.CompletableFuture;

public class Order {
    private static int idCounter = 0;

    private double initialQuantity;

    private final int clientId;
    private int orderId;
    private final Currency baseCurrency;
    private final Currency quoteCurrency;
    private final OrderType orderType;
    private final double price;
    private double quantity;
    private Status status;

    private final CompletableFuture<Callback> future;

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
        this.future = new CompletableFuture<>();
    }

    public static Order copy(Order other) {
        Order copy = new Order(other.clientId,
                other.baseCurrency,
                other.quoteCurrency,
                other.orderType,
                other.quantity,
                other.price);
        copy.orderId = other.orderId; // Сохраняем идентификатор оригинала
        copy.status = other.status; // Копируем статус
        copy.initialQuantity = other.initialQuantity; // Копируем начальное количество
        return copy;
    }

    public CompletableFuture<Callback> getFuture() {
        return future;
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

    public void setInitialQuantity(double initialQuantity) {
        this.initialQuantity = initialQuantity;
    }



    @Override
    public String toString() {
        return String.format("%s %s/%s: %.2f @ %.2f", orderType, baseCurrency, quoteCurrency, quantity, price);
    }
}
