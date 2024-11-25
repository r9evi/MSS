package api;

import currency.Currency;
import order.OrderType;
import response.Response;

public interface ServiceAPI {
    Response placeOrder(int clientId, OrderType type, Currency base, Currency target, double amount, double price);
    Response getOrderInfo(int clientId, int orderId);
    void closeExchange();
}
