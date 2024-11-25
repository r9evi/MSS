package currency;

import order.OrderBook;

import java.util.Objects;

public class CurrencyPair {
    private final Currency baseCurrency;
    private final Currency quoteCurrency;
    private final OrderBook orders;

    public CurrencyPair(Currency baseCurrency, Currency quoteCurrency, OrderBook orders) {
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.orders = orders;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public Currency getQuoteCurrency() {
        return quoteCurrency;
    }


    public String getPair() {
        return baseCurrency + "/" + quoteCurrency;
    }

    public String getInversePair() {
        return quoteCurrency + "/" + baseCurrency;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CurrencyPair that = (CurrencyPair) obj;
        return baseCurrency.equals(that.baseCurrency) && quoteCurrency.equals(that.quoteCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrency, quoteCurrency);
    }

//    @Override
//    public String toString() {
//        return getPair();
//    }
}
