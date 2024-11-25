package order;

import currency.Currency;

public class Order {
    private final Currency baseCurrency;
    private final Currency quoteCurrency;
    private final OrderType orderType;
    private final double price;
    private double quantity;

    public Order(Currency baseCurrency, Currency quoteCurrency, OrderType orderType, double quantity, double price) {
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
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

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%s %s/%s: %.2f @ %.2f", orderType, baseCurrency, quoteCurrency, quantity, price);
    }
}
