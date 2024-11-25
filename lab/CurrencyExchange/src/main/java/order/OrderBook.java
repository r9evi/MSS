package order;

import callback.Callback;
import callback.Status;
import request.Request;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderBook {
    private final LinkedList<Order> orders = new LinkedList<>();

    public void placeOrder(Order order, Request request) {
            for (int i = 0; i < orders.size(); i++) {
                Order existingOrder = orders.get(i);

                // Проверка совместимости валютной пары и типа ордера
                if (order.getId() != existingOrder.getId() && order.getBaseCurrency() == existingOrder.getQuoteCurrency() &&
                        order.getQuoteCurrency() == existingOrder.getBaseCurrency() &&
                        ((order.getOrderType() == OrderType.BUY && order.getPrice() >= 1 / existingOrder.getPrice()) ||
                                (order.getOrderType() == OrderType.SELL && order.getPrice() <= 1 / existingOrder.getPrice()))) {

                    double executedQuantity = Math.min(order.getQuantity(), existingOrder.getQuantity());
                    order.setQuantity(order.getQuantity() - executedQuantity);
                    existingOrder.setQuantity(existingOrder.getQuantity() - executedQuantity);

//                    System.out.printf("Executed: %.2f %s/%s @ %.2f\n", executedQuantity, order.getBaseCurrency(),
//                            order.getQuoteCurrency(), order.getPrice());

                    // Удаление полностью исполненного ордера
                    if (existingOrder.getQuantity() == 0) {
                        orders.get(i).setQuantity(0);
                        orders.remove(i);
                        i--;
                    }

                    // Если новый ордер исполнен, выходим
                    if (order.getQuantity() == 0) {
                        request.getFuture().complete(new Callback(Status.FULL_SUCCESS, order));
                        return;
                    }
                }
            }

            // Добавление оставшегося ордера
            addOrderInSortedPosition(order, request);
    }

    private void addOrderInSortedPosition(Order order, Request request) {
        int index = 0;
        while (index < orders.size()) {
            Order current = orders.get(index);

            // Сортировка по цене
            if (order.getOrderType() == OrderType.BUY && order.getPrice() > current.getPrice() ||
                    order.getOrderType() == OrderType.SELL && order.getPrice() < current.getPrice()) {
                break;
            }

            index++;
        }

        orders.add(index, order);
        request.getFuture().complete(new Callback(Status.PARTIAL_SUCCESS, order));
    }

    public void printOrderBook() {
        System.out.println("Order Book:");
        for (Order order : orders) {
            System.out.println(order);
        }
    }
}
