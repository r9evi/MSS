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

//    public void getOrderInfo(int clientId, int orderId, Currency base, Currency target, OrderType type,  Request request) {
//        int currencyIndex = calculateIndex(base, target);
//        pairs.get(currencyIndex).getOrders().getInfo(clientId, orderId, type, request);
//    }

    public  int calculateIndex(Currency base, Currency target) {
        if (base == null || target == null) {
            throw new IllegalArgumentException("Base and target currencies cannot be null");
        }
        int baseOrdinal = Math.min(base.ordinal(), target.ordinal());
        int targetOrdinal = Math.max(base.ordinal(), target.ordinal());
        int n = Currency.values().length;
        return baseOrdinal * (n - 1) - (baseOrdinal * (baseOrdinal - 1)) / 2 + (targetOrdinal - baseOrdinal - 1);
    }

}
