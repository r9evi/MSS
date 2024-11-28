package request;


import currency.Currency;
import order.OrderType;


public class PlaceOrderRequest {
    private final int clientId;
    private final OrderType orderType;
    private final Currency base;
    private final Currency target;
    private final double amount;
    private final double price;



    public PlaceOrderRequest(int clientId, OrderType orderType, Currency base,
                             Currency target, double amount, double price) {
        this.clientId = clientId;
        this.orderType = orderType;
        this.base = base;
        this.target = target;
        this.amount = amount;
        this.price = price;
    }

    public int getClientId() {
        return clientId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Currency getBase() {
        return base;
    }

    public Currency getTarget() {
        return target;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

}
