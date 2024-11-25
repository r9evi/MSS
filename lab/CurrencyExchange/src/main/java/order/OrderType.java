package order;

public enum OrderType {
    BUY("BUY"),
    SELL("SELL");

    private final String type;

    OrderType(String orderType) {
        this.type = orderType;
    }

    public String getOrderType() {
        return type;
    }
}
