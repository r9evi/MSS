package request;

import callback.Callback;
import currency.Currency;
import order.OrderType;


import java.util.concurrent.CompletableFuture;

public class OrderInfoRequest {
    private final int clientId;
    private final int orderId;
    private final Currency base;
    private final Currency target;
    private final OrderType type;


    public OrderInfoRequest(int clientId, int orderId, Currency base, Currency target, OrderType type) {
        this.clientId = clientId;
        this.orderId = orderId;
        this.base = base;
        this.target = target;
        this.type = type;
    }

    public int getClientId() {
        return clientId;
    }

    public int getOrderId() {
        return orderId;
    }

    public Currency getBase() {
        return base;
    }

    public Currency getTarget() {
        return target;
    }

    public OrderType getType() {
        return type;
    }
}
