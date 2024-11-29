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
        ExchangeAPI mss = Exchange.getInstance();

        Client client1 = new Client();
        client1.getWallet().deposit(Currency.USD, 1_000_000);

        Client client2 = new Client();
        client2.getWallet().deposit(Currency.RUB, 1_000_000);
        ClientService.addClient(client1);
        ClientService.addClient(client2);

        StringBuilder sb = new StringBuilder();

        double before = ClientService.calculateAllBalances();
        sb.append(String.format(MSG_FORMAT, "Before operation:", before));
        sb.append("""
                EXCHANGE
                ------------------""");
        System.out.println(sb);
        sb.setLength(0);
        long startTime = System.nanoTime(); // Время окончания
        Executor executor = Executors.newFixedThreadPool(50);
        executor.execute(() -> mss.placeOrder(client1.getId(), OrderType.SELL, Currency.USD, Currency.RUB, 500, 2));
        for (int i = 0; i < 1001; i ++) {
            if (i < 500) {
                executor.execute(() -> mss.placeOrder(client1.getId(), OrderType.SELL, Currency.USD, Currency.RUB, 500, 2));
            } else  {
                executor.execute(() -> mss.placeOrder(client2.getId(), OrderType.BUY, Currency.USD, Currency.RUB, 500, 2));
            }
        }
        Thread.sleep(100);
        long endTime = System.nanoTime(); // Время окончания
        System.out.println("Время выполнения: " + (endTime - startTime) / 100_000 + " наносекунд");
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
