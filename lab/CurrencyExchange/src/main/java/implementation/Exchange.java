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

        //StringBuilder sb = new StringBuilder();
        System.out.println(response.getMessage());
//        response.getFuture().thenAccept(callback -> {
//            if (callback.getResult() instanceof Order) {
//                if (callback.getStatus() == Status.FULL_SUCCESS || callback.getStatus() == Status.PARTIAL_SUCCESS) {
//                    sb.append("|-------ORDER INFO-------|\n")
//                            .append(callback).append("\n")
//                            .append("|------------------------|\n");
//                    System.out.println(sb);
//                    sb.setLength(0);
//                    ClientService.updateClientBalance((Order) callback.getResult());
//                }
//            } else {
//                sb.append("|-------ORDER INFO-------|\n")
//                        .append(callback).append("\n")
//                        .append("|------------------------|\n");
//                System.out.println(sb);
//                sb.setLength(0);
//            }
//        });
    }

}
