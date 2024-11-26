package implementation;

import api.ExchangeAPI;
import api.ServiceAPI;
import callback.Status;
import client.ClientService;
import currency.Currency;
import order.Order;
import order.OrderType;
import service.ExchangeService;

public class Exchange implements ExchangeAPI {
    private static Exchange instance;
    private static ServiceAPI service;

    private Exchange() {
        service = ExchangeService.getInstance();
    }

    public static Exchange getInstance() {
        if (instance == null) {
            instance = new Exchange();
        }
        return instance;
    }

    @Override
    public void placeOrder(int clientId, OrderType type, Currency base, Currency target, double amount, double price) {
        var response = service.placeOrder(clientId, type, base, target, amount, price);
        System.out.println(response.getMessage());
//        response.getFuture().thenAccept(System.out::println);
        response.getFuture().thenAccept(callback -> {
            System.out.println(callback);
            System.out.println("CLient ID: " + clientId);

            // Проверяем, что ордер выполнен
            if (callback.getStatus() == Status.FULL_SUCCESS || callback.getStatus() == Status.PARTIAL_SUCCESS) {
                Order order = (Order) callback.getResult();
                ClientService.updateClientBalance(order);
            }
        });
    }

    @Override
    public void getOrderInfo(int clientId, int orderId, Currency base, Currency target) {
        var response = service.getOrderInfo(clientId, orderId, base, target);
        System.out.println(response.getMessage());
        //response.getFuture().thenAccept(System.out::println);
        response.getFuture().thenAccept(callback -> {
            System.out.println(callback);
            System.out.println("Client ID: " + clientId);
            Order order = (Order) callback.getResult();
            ClientService.updateClientBalance(order);
        });
    }
}
