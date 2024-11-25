import currency.Currency;
import currency.CurrencyPair;
import currency.CurrencyPairs;
import order.OrderBook;

public class Runner {
    public static final void main(String[] args) {
        CurrencyPairs pairs = new CurrencyPairs();
        System.out.println(pairs.getAllPairs().size());

    }
}
