package implementation;

import api.ExchangeAPI;
import api.ServiceAPI;
import currency.Currency;
import order.OrderType;
import service.ExchangeService;

public class Exchange implements ExchangeAPI {
    private static Exchange instance;
    private static ServiceAPI service;

    private Exchange() {
        service = ExchangeService.getInstance();
    }

    public Exchange getInstacne() {
        if (instance == null) {
            instance = new Exchange();
        }
        return instance;
    }

    @Override
    public void placeOrder(int clientId, OrderType type, Currency base, Currency target, double amount, double price) {
        var response = service.placeOrder(clientId, type, base, target, amount, price);
        response.getFuture().thenAccept(System.out::println);
    }

    @Override
    public void getOrderInfo(int clientId, int orderId) {

    }
}
