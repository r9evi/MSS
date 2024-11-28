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

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Runner {
    public static final String MSG_FORMAT = "|-------------------|\n%s %.2f\n|-------------------|\n";
    public static void main(String[] args) throws InterruptedException {
        // Инициализация биржи и клиентов
        ExchangeAPI mss = Exchange.getInstance();

        Client client1 = new Client();
        //client1.getWallet().deposit(Currency.RUB, 50_000);
        client1.getWallet().deposit(Currency.USD, 12_500);

        Client client2 = new Client();
        client2.getWallet().deposit(Currency.RUB, 25_000);
        //client2.getWallet().deposit(Currency.USD, 50_000);
        ClientService.addClient(client1);
        ClientService.addClient(client2);

        StringBuilder sb = new StringBuilder();

       // sb.append("|-------------------|");
        double before = ClientService.calculateAllBalances();
        sb.append(String.format(MSG_FORMAT, "Before operation:", before));
        sb.append("""
                EXCHANGE
                ------------------""");
        System.out.println(sb);
        sb.setLength(0);


        Executor executor = Executors.newFixedThreadPool(50);
//        executor.execute(() -> {
//            int i = 0;
//            while (i < 100) {
//                i++;
//                mss.placeOrder(client1.getId(), OrderType.SELL, Currency.USD, Currency.EUR, 1000, 1.5);
//            }
//
//
//            //mss.getOrderInfo(0, 0, Currency.USD, Currency.EUR, OrderType.SELL);
//        });
//        Thread.sleep(2);
//        ExchangeService.getInstance().closeExchange();



        for (int i = 0; i < 51; i ++) {
//            if (i == 1) {
//                executor.execute(() -> mss.placeOrder(client1.getId(), OrderType.SELL, Currency.USD, Currency.EUR, 1000, 1.5));
//            }
            if (i < 25) {
                executor.execute(() -> mss.placeOrder(client1.getId(), OrderType.SELL, Currency.USD, Currency.RUB, 500, 2));
            } else  {
                executor.execute(() -> mss.placeOrder(client2.getId(), OrderType.BUY, Currency.USD, Currency.RUB, 500, 2));
            }
        }
        Thread.sleep(100);
        ClientService.updateClientsBalances();
        double after = ClientService.calculateAllBalances();
        sb.append(String.format(MSG_FORMAT, "After operation:", after));
        System.out.println(sb);
        sb.setLength(0);
        if (Double.compare(before, after) == 0) {
            System.out.println("Суммарный баланс остался неизменным.");
        } else {
            System.err.println("Ошибка: Баланс изменился!");
        }

    }


}
