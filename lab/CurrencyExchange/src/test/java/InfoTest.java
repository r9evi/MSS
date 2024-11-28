import api.ExchangeAPI;
import client.Client;
import client.ClientService;
import currency.Currency;
import implementation.Exchange;
import order.OrderType;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InfoTest {
    StringBuilder sb = Printer.sb;
    public static final String MSG_FORMAT = "|-------------------|\n%s %.2f\n|-------------------|\n";

    @Test
    public void getOrderInfoTest() {
        ExchangeAPI mss = Exchange.getInstance();
        Client client5 = new Client();
        client5.getWallet().deposit(Currency.USD, 1_000_000);
        ClientService.addClient(client5);
        sb.append("""
                EXCHANGE
                ------------------""");
        Printer.print();

        Executor executor = Executors.newFixedThreadPool(2);
        executor.execute(() -> mss.placeOrder(client5.getId(), OrderType.SELL, Currency.USD, Currency.EUR, 500, 2));
        try {
            Thread.sleep(100);
            executor.execute(() -> mss.getOrderInfo(client5.getId(), client5.getId(), Currency.USD, Currency.RUB, OrderType.SELL));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
