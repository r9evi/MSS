package api;

import currency.Currency;
import order.Order;
import order.OrderType;

public interface ExchangeAPI {
    void placeOrder(int clientId, OrderType type, Currency base, Currency target, double amount, double price);
    //void getOrderInfo(int clientId, int orderId, Currency base, Currency target, OrderType type);
}
