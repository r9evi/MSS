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
        //System.out.println(response.getMessage());
        //response.getFuture().thenAccept(System.out::println);
        StringBuilder sb = new StringBuilder();
        response.getFuture().thenAccept(callback -> {
            if (callback.getResult() instanceof Order) {
                if (callback.getStatus() == Status.FULL_SUCCESS || callback.getStatus() == Status.PARTIAL_SUCCESS) {
                    sb.append("|-------ORDER INFO-------|\n")
                            .append(callback).append("\n")
                            .append("|------------------------|\n");
                    System.out.println(sb);
                    sb.setLength(0);
//                    System.out.println("|-------ORDER INFO-------|");
//                    System.out.println(callback.getStatus());
//                    System.out.println("|------------------------|");
                    ClientService.updateClientBalance((Order) callback.getResult());
                }
            } else {
//                System.out.println(callback.getStatus());
                sb.append("|-------ORDER INFO-------|\n")
                        .append(callback).append("\n")
                        .append("|------------------------|\n");
                System.out.println(sb);
                sb.setLength(0);
               //System.out.printf("%s\n%n", callback);
            }
        });
    }

    @Override
    public void getOrderInfo(int clientId, int orderId, Currency base, Currency target, OrderType type) {
        var response = service.getOrderInfo(clientId, orderId, base, target, type);
        //System.out.println(response.getMessage());
        StringBuilder sb = new StringBuilder();
        response.getFuture().thenAccept(callback -> {
            if (callback.getResult() instanceof Order order) {
                sb.append("|-------INFO-------|\n")
                        .append(" Type: ").append(order.getOrderType())
                        .append("\n Base: ").append(order.getBaseCurrency())
                        .append("\n Target: ").append(order.getQuoteCurrency())
                        .append("\n Quantity: ").append(order.getQuantity())
                        .append("\n Price: ").append(order.getPrice())
                        .append("\n|-------INFO-------|\n");
                System.out.println(sb);
                sb.setLength(0);
            } else {
                System.out.println("Статус: " + callback.getStatus() + "\nРезультат: " + callback.getResult());
            }

        });
    }
}
