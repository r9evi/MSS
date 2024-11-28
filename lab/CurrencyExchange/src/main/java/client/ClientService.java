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
                        //sb.append(callback.getResult()).append("\n");
                        //System.out.println(callback.getResult());
                        if (callback.getResult() instanceof Order) {
                            sb.append("|-------ORDER INFO-------|\n")
                                    .append(callback).append("\n")
                                    .append("|------------------------|\n");
//                            System.out.println("|-------ORDER INFO-------|");
//                            System.out.println(callback.getStatus());
//                            System.out.println("|------------------------|");
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
        double executedQuantity = order.getInitialQuantity() - order.getQuantity(); // Исполненная часть

        if (executedQuantity > 0) {
            double totalCost = executedQuantity * order.getPrice();
            Currency base = order.getBaseCurrency();
            Currency quote = order.getQuoteCurrency();

            // Если ордер на покупку (BUY), то покупатель платит котируемой валютой и получает базовую
            if (order.getOrderType() == OrderType.BUY) {
                // Снимаем котируемую валюту (платеж)
                wallet.withdraw(quote, totalCost);
                // Добавляем базовую валюту (покупка)
                wallet.deposit(base, executedQuantity);
            }
            // Если ордер на продажу (SELL), то продавец продает базовую валюту и получает котируемую
            else if (order.getOrderType() == OrderType.SELL) {
                // Снимаем базовую валюту (продажа)
                wallet.withdraw(base, executedQuantity);
                // Добавляем котируемую валюту (получение)
                wallet.deposit(quote, totalCost);
            }
        }
    }


}
