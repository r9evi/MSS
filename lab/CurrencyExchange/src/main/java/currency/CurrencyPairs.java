package currency;

import order.Order;
import order.OrderBook;
import request.Request;

import java.util.ArrayList;
import java.util.List;

public class CurrencyPairs {
    private final List<CurrencyPair> pairs;

    public CurrencyPairs() {
        pairs = new ArrayList<>();
        fillWithPairs();
    }

    /**
     * Инициализация списка валютных пар.
     * Создаёт все уникальные комбинации пар валют из перечисления Currency.
     */
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
        CurrencyPair currency = pairs.get(currencyIndex);
        currency.placeOrderToOrderBook(order, request);
    }

    /**
     * Вычисляет уникальный индекс для пары валют.
     * Упорядочивает валюты, чтобы избежать дублирования (USD/RUB = RUB/USD).
     * @param pair Пара валют.
     * @return Уникальный индекс пары.
     */
    public int calculateIndex(CurrencyPair pair) {
        if (pair == null) {
            throw new IllegalArgumentException("CurrencyPair cannot be null");
        }

        int baseOrdinal = pair.getBaseCurrency().ordinal();
        int quoteOrdinal = pair.getQuoteCurrency().ordinal();

        // Упорядочиваем валюты, чтобы избежать дублирования
        int minOrdinal = Math.min(baseOrdinal, quoteOrdinal);
        int maxOrdinal = Math.max(baseOrdinal, quoteOrdinal);

        int n = Currency.values().length;
        return minOrdinal * n + maxOrdinal;
    }

    public int calculateIndex(Currency base, Currency target) {
        if (base == null || target == null) {
            throw new IllegalArgumentException("Base and target currencies cannot be null");
        }

        int baseOrdinal = base.ordinal();
        int targetOrdinal = target.ordinal();

        // Упорядочиваем валюты, чтобы избежать дублирования
        int minOrdinal = Math.min(baseOrdinal, targetOrdinal);
        int maxOrdinal = Math.max(baseOrdinal, targetOrdinal);

        int n = Currency.values().length;
        return minOrdinal * n + maxOrdinal;
    }

    /**
     * Получает валютную пару по уникальному индексу.
     * Используется обратный расчёт индексов, чтобы восстановить валюты.
     * @param index Уникальный индекс пары.
     * @return Найденная или созданная пара.
     */
    public CurrencyPair getPairByIndex(int index) {
        int n = Currency.values().length;
        int baseIndex = index / n;
        int quoteIndex = index % n;

        Currency base = Currency.values()[baseIndex];
        Currency quote = Currency.values()[quoteIndex];

        return new CurrencyPair(base, quote, null); // Возвращаем новую или существующую пару
    }

    /**
     * Возвращает список всех валютных пар.
     * @return Список уникальных валютных пар.
     */
    public List<CurrencyPair> getAllPairs() {
        return new ArrayList<>(pairs); // Возвращаем копию списка для безопасности
    }

    /**
     * Получает пару валют по указанным базовой и котируемой валютам.
     * Упорядочивает базовую и котируемую валюты, чтобы избежать дублирования (USD/RUB = RUB/USD).
     * @param base Базовая валюта.
     * @param target Котируемая валюта.
     * @return Найденная или созданная пара.
     */
    public CurrencyPair getPair(Currency base, Currency target) {
        if (base == null || target == null) {
            throw new IllegalArgumentException("Base and target currencies cannot be null");
        }

        // Упорядочиваем валюты
        Currency minCurrency = base.ordinal() < target.ordinal() ? base : target;
        Currency maxCurrency = base.ordinal() < target.ordinal() ? target : base;

        // Ищем существующую пару
        for (CurrencyPair pair : pairs) {
            if (pair.getBaseCurrency() == minCurrency && pair.getQuoteCurrency() == maxCurrency) {
                return pair;
            }
        }

        // Если пара не найдена, создаём новую
        CurrencyPair newPair = new CurrencyPair(minCurrency, maxCurrency, null);
        pairs.add(newPair);
        return newPair;
    }
}
