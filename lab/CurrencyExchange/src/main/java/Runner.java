import api.ExchangeAPI;
import api.ServiceAPI;
import client.Client;
import client.ClientService;
import currency.Currency;
import currency.CurrencyPair;
import currency.CurrencyPairs;
import implementation.Exchange;
import order.OrderBook;
import order.OrderType;
import service.ExchangeService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        ExchangeAPI mss = Exchange.getInstance();
        ClientService clientService = ClientService.getInstance();
        for (int i = 0; i < 2; i++) {
            Client client = new Client();
            client.getWallet().deposit(Currency.RUB, 1000.0);
            client.getWallet().deposit(Currency.USD, 500);
            clientService.addClient(client);
        }
        System.out.println("|-------------------|");
        double before = clientService.calculateAllBalances();
        System.out.println("Before operation: " + before);
        System.out.println("|-------------------|\n" + "EXCHANGE\n" + "------------------");
        try (ExecutorService executorService = Executors.newFixedThreadPool(50)) {
            for (int i = 0; i < 2; i++) {
                int finalI = i;
                executorService.execute(() -> {
                    mss.placeOrder(clientService.getClientId(finalI), OrderType.BUY, Currency.RUB,
                            Currency.USD, 500, 4);
                    mss.placeOrder(clientService.getClientId(finalI), OrderType.SELL, Currency.USD,
                            Currency.RUB, 1000, 2);
                });

            }
        }
        try {
            Thread.sleep(100);
            mss.getOrderInfo(0, 2, Currency.RUB, Currency.USD);
            mss.getOrderInfo(1, 3, Currency.USD, Currency.RUB);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Thread.sleep(100);
        System.out.println("|-------------------|");
        double after = clientService.calculateAllBalances();
        System.out.println("After operation: " + after);
        System.out.println("|-------------------|");
        boolean res = before == after;
        System.out.printf("Integrity %s\n", res);

    }
}
