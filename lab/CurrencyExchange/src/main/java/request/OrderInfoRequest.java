package request;

import callback.Callback;

import java.util.concurrent.CompletableFuture;

public class OrderInfoRequest {
    private final int clientId;
    private final int orderId;


    public OrderInfoRequest(int clientId, int orderId) {
        this.clientId = clientId;
        this.orderId = orderId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getOrderId() {
        return orderId;
    }

}
