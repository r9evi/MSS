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
    public static void main(String[] args) {
        ExchangeAPI mss = Exchange.getInstance();
        ClientService clientService = ClientService.getInstance();
        for (int i=0; i < 50; i++) {
            clientService.addClient(new Client());
        }
        try (ExecutorService executorService = Executors.newFixedThreadPool(50)) {

                try {
                    for (int i = 0; i < 50; i++) {
                        int finalI = i;
                        if (i == 25) {
                            ExchangeService.getInstance().closeExchange();
                        }
                        executorService.execute(() -> {
                            mss.placeOrder(clientService.getClientId(finalI), OrderType.BUY, Currency.RUB,
                                    Currency.USD, 500, 100);
                            mss.placeOrder(clientService.getClientId(finalI), OrderType.SELL, Currency.USD,
                                    Currency.RUB, 1000, 5);
                        });

                    }
                } finally {
                    executorService.shutdown();

                }


            }
        }
    }
