package client;

import currency.Currency;
import order.Order;
import order.OrderType;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

public class ClientService {
    private static List<Client> clients;

    private static ClientService instance;

    private ClientService() {
        clients = new ArrayList<>();
    }

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public double calculateAllBalances() {
        double sum = 0;
        for (Client client : clients) {
            sum += client.getWallet().getTotal();
        }
        return sum;
    }
    public List<Client> getClients() {
        return clients;
    }

    public int getClientId(int index) {
        return clients.get(index).getId();
    }

    public Client getClient(int clientId) {
        return clients.get(clientId);
    }

    public void addClient(Client client) {
        if (!clients.contains(client)) {
            clients.add(client);
        }
    }



//    public static synchronized void updateClientBalance(int clientId, Currency base, Currency target, double remainingTargetAmount, double executedAmount, double price, OrderType type) {
//        Wallet wallet = clients.get(clientId).getWallet();
//        double bought = executedAmount - remainingTargetAmount;
//        if (type == OrderType.BUY) {
//            wallet.withdraw(base, bought * price);
//            wallet.deposit(target, bought);
//        } else if (type == OrderType.SELL) {
//            wallet.withdraw(base, bought / price);
//            wallet.deposit(target, bought);
//        }
//    }
public static synchronized void updateClientBalance(Order order) {
    // Получаем клиента и его кошелек
    Wallet wallet = clients.get(order.getClientId()).getWallet();
    // Вычисления
    double boughtQuantity = order.getInitialQuantity() - order.getQuantity();
    if (boughtQuantity > 0) {
        double orderPrice = order.getPrice();
        Currency base = order.getBaseCurrency();
        Currency target = order.getQuoteCurrency();

        if (order.getOrderType().equals(OrderType.BUY)) {
            double totalCost = boughtQuantity * orderPrice;
            wallet.withdraw(base, totalCost);
            wallet.deposit(target, boughtQuantity);
        } else {
            double amountSold = boughtQuantity / orderPrice;
            wallet.withdraw(base, amountSold);
            wallet.deposit(target, boughtQuantity);
        }
    }
}


}
