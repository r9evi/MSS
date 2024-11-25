package currency;

public enum Currency {
    RUB("RUB"),
    USD("USD"),
    EUR("EUR");

    private final String currency;

    Currency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public static int getCurrencyPairsAmount() {
        return (Currency.values().length * (Currency.values().length - 1)) / 2;
    }
}
