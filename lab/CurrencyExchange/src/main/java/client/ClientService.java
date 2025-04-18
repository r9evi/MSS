package client;

import callback.Callback;
import currency.Currency;
import order.Order;
import order.OrderType;

import java.util.*;

import java.util.concurrent.CompletableFuture;

public class ClientService {
    private static final Map<Integer, List<CompletableFuture<Callback>>> clientsOrdersHistory = new HashMap<>();
    private static final List<Client> clients = new ArrayList<>();


    public static synchronized double calculateAllBalances() {
        double sum = 0;
        for (Client client : clients) {
            sum += client.getWallet().getTotal();
        }
        return sum;
    }


    public synchronized static Client getClient(int clientId) {
        return clients.get(clientId);
    }

    public synchronized static void addClient(Client client) {
        clients.add(client);
        clientsOrdersHistory.put(client.getId(), new ArrayList<>());
    }

    public synchronized static void addClientEvent(int clientId, CompletableFuture<Callback> future) {
        clientsOrdersHistory.get(clientId).add(future);
    }

    public synchronized static void updateClientsBalances() {
        int i = 0;
        for (Map.Entry<Integer, List<CompletableFuture<Callback>>> entry : clientsOrdersHistory.entrySet()) {
            var list = entry.getValue();
            for (CompletableFuture<Callback> future : list) {
                if (future != null) {
                    StringBuilder sb = new StringBuilder();
                    future.thenAccept(callback -> {
                        if (callback.getResult() instanceof Order) {
                            sb.append("|-------ORDER INFO-------|\n")
                                    .append(callback).append("\n")
                                    .append("|------------------------|\n");
                            System.out.println(sb);
                            sb.setLength(0);
                            updateClientBalance((Order) callback.getResult());

                        }
                    });
                } else {
                    i++;
                    System.out.println("Ошибка: " + i);
                }
            }
        }
    }


    public static synchronized void updateClientBalance(Order order) {
        Wallet wallet = clients.get(order.getClientId()).getWallet();
        double executedQuantity = order.getInitialQuantity() - order.getQuantity();

        if (executedQuantity > 0) {
            double totalCost = executedQuantity * order.getPrice();
            Currency base = order.getBaseCurrency();
            Currency quote = order.getQuoteCurrency();

            if (order.getOrderType() == OrderType.BUY) {
                wallet.withdraw(quote, totalCost);
                wallet.deposit(base, executedQuantity);
            }
            else if (order.getOrderType() == OrderType.SELL) {
                wallet.withdraw(base, executedQuantity);
                wallet.deposit(quote, totalCost);
            }
        }
    }


}
