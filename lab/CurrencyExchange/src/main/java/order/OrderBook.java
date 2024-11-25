package order;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderBook {
    private LinkedList<Order> orders = new LinkedList<>();

    private final Lock lock = new ReentrantLock();

    public void placeOrder(Order order) {
        lock.lock();
        try {
            //
        } finally {
            lock.unlock();
        }
//        System.out.println("Placing Order: " + order);
//
//        for (int i = 0; i < orders.size(); i++) {
//            Order existingOrder = orders.get(i);
//
//            // Проверяем, может ли ордер быть исполнен
//            if ((order.type == Order.Type.BUY && existingOrder.type == Order.Type.SELL && order.price >= existingOrder.price) ||
//                    (order.type == Order.Type.SELL && existingOrder.type == Order.Type.BUY && order.price <= existingOrder.price)) {
//
//                int executedQuantity = Math.min(order.quantity, existingOrder.quantity);
//                order.quantity -= executedQuantity;
//                existingOrder.quantity -= executedQuantity;
//
//                System.out.println("Executed: " + executedQuantity + " @ " + existingOrder.price);
//
//                // Если встречный ордер полностью исполнен, удаляем его
//                if (existingOrder.quantity == 0) {
//                    orders.remove(i);
//                    i--; // Сдвигаем индекс назад из-за удаления элемента
//                }
//
//                // Если новый ордер полностью исполнен, выходим
//                if (order.quantity == 0) {
//                    return;
//                }
//            }
//        }
//
//        // Если осталась часть нового ордера, добавляем его в стакан
//        addOrderInSortedPosition(order);
    }

    private void addOrderInSortedPosition(Order order) {
//        int index = 0;
//        while (index < orders.size()) {
//            Order current = orders.get(index);
//
//            // Для покупок: сортируем по убыванию цены
//            if (order.type == Order.Type.BUY && (current.type == Order.Type.SELL || order.price > current.price)) {
//                break;
//            }
//
//            // Для продаж: сортируем по возрастанию цены
//            if (order.type == Order.Type.SELL && (current.type == Order.Type.BUY || order.price < current.price)) {
//                break;
//            }
//
//            index++;
//        }
//
//        // Вставляем ордер в найденную позицию
//        orders.add(index, order);
    }

    public void printOrderBook() {
        System.out.println("Order Book:");
        for (Order order : orders) {
            System.out.println(order);
        }
    }
}
