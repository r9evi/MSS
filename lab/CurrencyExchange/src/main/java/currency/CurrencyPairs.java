package currency;

import order.Order;
import order.OrderBook;
import order.OrderType;
import request.Request;

import java.util.ArrayList;
import java.util.List;

public class CurrencyPairs {
    private final List<CurrencyPair> pairs;

    public CurrencyPairs() {
        pairs = new ArrayList<>();
        fillWithPairs();
    }

    private void fillWithPairs() {
        Currency[] currencies = Currency.values();
        for (int i = 0; i < currencies.length; i++) {
            for (int j = i + 1; j < currencies.length; j++) {
                pairs.add(new CurrencyPair(currencies[i], currencies[j], new OrderBook())); // Используйте реальный OrderBook, если доступен
            }
        }
    }

    public void placeCurrencyPairOrder(Order order, Request request) {
        int currencyIndex = calculateIndex(order.getBaseCurrency(), order.getQuoteCurrency());
        pairs.get(currencyIndex).placeOrderToOrderBook(order, request);
    }

    public void getOrderInfo(int clientId, int orderId, Currency base, Currency target, OrderType type,  Request request) {
        int currencyIndex = calculateIndex(base, target);
        pairs.get(currencyIndex).getOrders().getInfo(clientId, orderId, type, request);
    }

    public  int calculateIndex(CurrencyPair pair) {
        if (pair == null) {
            throw new IllegalArgumentException("CurrencyPair cannot be null");
        }
        int baseOrdinal = pair.getBaseCurrency().ordinal();
        int quoteOrdinal = pair.getQuoteCurrency().ordinal();
        int minOrdinal = Math.min(baseOrdinal, quoteOrdinal);
        int maxOrdinal = Math.max(baseOrdinal, quoteOrdinal);
        int n = Currency.values().length;
        return minOrdinal * n + maxOrdinal;
    }

    public  int calculateIndex(Currency base, Currency target) {
        if (base == null || target == null) {
            throw new IllegalArgumentException("Base and target currencies cannot be null");
        }
        int baseOrdinal = Math.min(base.ordinal(), target.ordinal());
        int targetOrdinal = Math.max(base.ordinal(), target.ordinal());
        int n = Currency.values().length;
        return baseOrdinal * (n - 1) - (baseOrdinal * (baseOrdinal - 1)) / 2 + (targetOrdinal - baseOrdinal - 1);
    }

    public CurrencyPair getPairByIndex(int index) {
        int n = Currency.values().length;
        int baseIndex = index / n;
        int quoteIndex = index % n;
        Currency base = Currency.values()[baseIndex];
        Currency quote = Currency.values()[quoteIndex];
        return new CurrencyPair(base, quote, null);
    }

    public List<CurrencyPair> getAllPairs() {
        return new ArrayList<>(pairs);
    }

    public CurrencyPair getPair(Currency base, Currency target) {
        if (base == null || target == null) {
            throw new IllegalArgumentException("Base and target currencies cannot be null");
        }
        Currency minCurrency = base.ordinal() < target.ordinal() ? base : target;
        Currency maxCurrency = base.ordinal() < target.ordinal() ? target : base;
        for (CurrencyPair pair : pairs) {
            if (pair.getBaseCurrency() == minCurrency && pair.getQuoteCurrency() == maxCurrency) {
                return pair;
            }
        }
        CurrencyPair newPair = new CurrencyPair(minCurrency, maxCurrency, null);
        pairs.add(newPair);
        return newPair;
    }
}
