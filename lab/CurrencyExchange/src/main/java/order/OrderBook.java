package order;

import callback.Callback;
import callback.Status;
import request.Request;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderBook {
    private final PriorityQueue<Order> buyOrders;
    private final PriorityQueue<Order> sellOrders;



    public OrderBook() {
        // Очередь для покупок: убывающий порядок цены (максимальная цена имеет приоритет)
        buyOrders = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice).reversed());
        // Очередь для продаж: возрастающий порядок цены (минимальная цена имеет приоритет)
        sellOrders = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice));
    }


    public void getInfo(int clientId, int orderId, OrderType type, Request request) {
        if (type.equals(OrderType.BUY)) {
            findOrderByInfo(clientId, orderId, buyOrders, request);
        } else {
            findOrderByInfo(clientId, orderId, sellOrders, request);
        }



    }

    public void findOrderByInfo(int clientId, int orderId, Queue<Order> orders, Request request) {
        for (Order order : orders) {
            if (order.getOrderId() == clientId && order.getOrderId() == orderId) {
                request.getFuture().complete(new Callback(order.getStatus(), order));
            } else {
                request.getFuture().complete(new Callback(Status.FAILURE, "Ордер не найден"));
            }
        }
    }



    public void placeOrder(Order newOrder, Request request) {
        // В зависимости от типа ордера (покупка или продажа) передаем нужный список ордеров
        if (newOrder.getOrderType() == OrderType.BUY) {
            if (sellOrders.isEmpty()) {
                buyOrders.offer(newOrder);
                request.getFuture().complete(new Callback(Status.CREATED, "Ордер размещен"));
            } else {
                processOrder(newOrder, request, sellOrders);
            }
        } else {
            if (buyOrders.isEmpty()) {
                sellOrders.offer(newOrder);
                request.getFuture().complete(new Callback(Status.CREATED, "Ордер размещен"));
            } else {
                processOrder(newOrder, request, buyOrders);
            }
        }
    }

    // Универсальный метод для обработки как ордеров на покупку, так и ордеров на продажу
    private void processOrder(Order newOrder, Request request, Queue<Order> oppositeOrders) {
        Iterator<Order> iterator = oppositeOrders.iterator();

        while (iterator.hasNext() && newOrder.getQuantity() > 0) {
            Order existingOrder = iterator.next();

            // Проверяем, можно ли выполнить сделку
            if (canExecute(newOrder, existingOrder)) {
                // Рассчитываем, сколько валюты может быть куплено/продано
                double quantity = Math.min(newOrder.getQuantity(), existingOrder.getQuantity());

                // Обновляем количество ордеров
                newOrder.setQuantity(newOrder.getQuantity() - quantity);
                existingOrder.setQuantity(existingOrder.getQuantity() - quantity);

                // Обновляем статус противоположного ордера
                if (existingOrder.getQuantity() <= 0) {
                    existingOrder.setStatus(Status.FULL_SUCCESS);
                    existingOrder.getFuture().complete(new Callback(existingOrder.getStatus(), existingOrder));
                    //Order.copy(existingOrder)
                    iterator.remove();  // Удаляем ордер, если он выполнен полностью
                } else {
                    existingOrder.setStatus(Status.PARTIAL_SUCCESS);
                   // existingOrder.setInitialQuantity(existingOrder.getQuantity());
                    //System.out.println("Статус противоположного ордера: " + existingOrder.getStatus());
                }

                // Обновляем статус текущего ордера
                if (newOrder.getQuantity() <= 0) {
                    newOrder.setStatus(Status.FULL_SUCCESS);
                    request.getFuture().complete(new Callback(Status.FULL_SUCCESS, newOrder));
                } else {
                    Order oldOrderCopy = Order.copy(newOrder);
                    newOrder.setInitialQuantity(newOrder.getQuantity());
                    if (newOrder.getOrderType().equals(OrderType.SELL)) {
                       sellOrders.add(newOrder);
                    } else {
                        buyOrders.add(newOrder);
                    }
                   // oppositeOrders.add(newOrder);
                    request.getFuture().complete(new Callback(Status.PARTIAL_SUCCESS, oldOrderCopy));
                }
                return;  // Ордер полностью выполнен, можно завершить обработку
            }
        }
        newOrder.setStatus(Status.CREATED);
        request.getFuture().complete(new Callback(Status.CREATED, "Не найдено встречных. Размещен"));
    }

    // Метод для проверки условий исполнения ордера
    private boolean canExecute(Order newOrder, Order existingOrder) {
        // Условие самостоятельной торговли (ордеры одного клиента не могут быть выполнены)
        if (newOrder.getClientId() == existingOrder.getClientId()) {
            return false;
        }
        // Проверка совместимости цен
        if (newOrder.getOrderType() == OrderType.BUY) {
            return newOrder.getPrice() >= existingOrder.getPrice(); // Покупатель готов платить достаточно
        } else if (newOrder.getOrderType() == OrderType.SELL) {
            return newOrder.getPrice() <= existingOrder.getPrice(); // Продавец согласен продать по цене покупателя
        }
        return false;
    }


    public static void printOrderBook() {
//        System.out.println("Buy Orders:");
//        buyOrders.forEach(System.out::println);
//
//        System.out.println("Sell Orders:");
//        sellOrders.forEach(System.out::println);
    }
}
