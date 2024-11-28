import api.ExchangeAPI;
import client.Client;
import client.ClientService;
import currency.Currency;
import implementation.Exchange;
import order.OrderType;
import org.junit.jupiter.api.Test;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PartialCompleteTest {

    StringBuilder sb = Printer.sb;
    public static final String MSG_FORMAT = "|-------------------|\n%s %.2f\n|-------------------|\n";


    @Test
    public void testSystemIntegrityPartialCompletedOrders() {
        ExchangeAPI mss = Exchange.getInstance();
        Client client3 = new Client();
        Client client4 = new Client();
        client3.getWallet().deposit(Currency.USD, 1_000_000);
        client4.getWallet().deposit(Currency.EUR, 1_000_000);
        ClientService.addClient(client3);
        ClientService.addClient(client4);

        double before1 = ClientService.calculateAllBalances();
        sb.append(String.format(MSG_FORMAT, "Before operation:", before1));
        sb.append("""
                EXCHANGE
                ------------------""");
        Printer.print();

        Executor executor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 1001; i ++) {
            if (i < 750) {
                executor.execute(() -> mss.placeOrder(client3.getId(), OrderType.SELL, Currency.USD, Currency.EUR, 500, 2));
            } else  {
                executor.execute(() -> mss.placeOrder(client4.getId(), OrderType.BUY, Currency.USD, Currency.EUR, 1000, 2));
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ClientService.updateClientsBalances();
        double after1 = ClientService.calculateAllBalances();
        sb.append(String.format(MSG_FORMAT, "After operation:", after1));
        System.out.println(sb);
        sb.setLength(0);

        assertEquals(before1, after1);
    }


}
