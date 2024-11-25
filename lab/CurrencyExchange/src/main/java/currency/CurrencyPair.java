package currency;

import order.Order;
import order.OrderBook;
import request.Request;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CurrencyPair {
    private final Currency baseCurrency;
    private final Currency quoteCurrency;
    private final OrderBook orders;

    private final Lock lock = new ReentrantLock();

    public CurrencyPair(Currency baseCurrency, Currency quoteCurrency, OrderBook orders) {
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.orders = orders;
    }

    public void placeOrderToOrderBook(Order order, Request request) {
        lock.lock();
        try {
            orders.placeOrder(order, request);
        } finally {
            lock.unlock();
        }
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

    public OrderBook getOrders() {
        return orders;
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
