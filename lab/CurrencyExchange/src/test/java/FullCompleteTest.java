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

public class FullCompleteTest {
    StringBuilder sb = Printer.sb;
    public static final String MSG_FORMAT = "|-------------------|\n%s %.2f\n|-------------------|\n";

    @Test
    public void testSystemIntegrityFullCompletedOrders() {
        ExchangeAPI mss = Exchange.getInstance();
        Client client1 = new Client();
        Client client2 = new Client();
        client1.getWallet().deposit(Currency.USD, 1_000_000);
        client2.getWallet().deposit(Currency.RUB, 1_000_000);
        ClientService.addClient(client1);
        ClientService.addClient(client2);

        double before = ClientService.calculateAllBalances();
        sb.append(String.format(MSG_FORMAT, "Before operation:", before));
        sb.append("""
                EXCHANGE
                ------------------""");
        Printer.print();

        Executor executor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 1001; i ++) {
            if (i < 500) {
                executor.execute(() -> mss.placeOrder(client1.getId(), OrderType.SELL, Currency.USD, Currency.RUB, 500, 2));
            } else  {
                executor.execute(() -> mss.placeOrder(client2.getId(), OrderType.BUY, Currency.USD, Currency.RUB, 500, 2));
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ClientService.updateClientsBalances();
        double after = ClientService.calculateAllBalances();
        sb.append(String.format(MSG_FORMAT, "After operation:", after));
        System.out.println(sb);
        sb.setLength(0);
        assertEquals(before, after);
    }

}
