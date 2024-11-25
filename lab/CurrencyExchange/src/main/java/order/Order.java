package order;


public class Order {
    private final OrderType orderType;
    private final int clientId;
    private double amount;
    private final double price;

    public Order(double price, double amount, int clientId, OrderType orderType) {
        this.price = price;
        this.amount = amount;
        this.clientId = clientId;
        this.orderType = orderType;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public int getClientId() {
        return clientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }
}
