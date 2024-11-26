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

    private final List<Order> completedOrders;


    public OrderBook() {
        // Очередь для покупок: убывающий порядок цены (максимальная цена имеет приоритет)
        buyOrders = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice).reversed());

        completedOrders = new ArrayList<>();
        // Очередь для продаж: возрастающий порядок цены (минимальная цена имеет приоритет)
        sellOrders = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice));
    }


    public void getInfo(int clientId, int orderId, Request request) {
        Order order = findCompletedOrderByClientIdAndOrderId(clientId, orderId);
        if (order != null) {
            request.getFuture().complete(new Callback(order.getStatus(), order));
        } else {
            request.getFuture().complete(new Callback(Status.FAILURE, "Ордер не найден"));
        }


    }

//    public Order findOrderInQueue(OrderType type, long clientId, long orderId) {
//        if (type.equals(OrderType.BUY)) {
//            for (Order order : buyOrders) {
//                if (order.getClientId() == clientId && order.getOrderId() == orderId) {
//                    return order;  // Возвращаем найденный ордер
//                }
//            }
//        } else {
//            for (Order order : sellOrders) {
//                if (order.getClientId() == clientId && order.getOrderId() == orderId) {
//                    return order;  // Возвращаем найденный ордер
//                }
//            }
//        }
//        // Перебираем все ордера в очереди
//
//
//        return null;  // Возвращаем null, если ордер не найден
//    }

    public Order findCompletedOrderByClientIdAndOrderId(long clientId, long orderId) {
        for (Order order : completedOrders) {
            if (order.getClientId() == clientId && order.getOrderId() == orderId) {
                completedOrders.remove(order); // Удаляем найденный ордер из списка
                return order;  // Возвращаем найденный ордер
            }
        }
        return null;  // Возвращаем null, если ордер не найден
    }


    public void placeOrder(Order newOrder, Request request) {
        if (newOrder.getOrderType() == OrderType.BUY) {
            if (sellOrders.isEmpty()) {
                buyOrders.offer(newOrder);
            } else {
                executeBuyOrder(newOrder, request);
            }
        } else {
            if (buyOrders.isEmpty()) {
                sellOrders.offer(newOrder);
            } else {
                executeSellOrder(newOrder, request);
            }
        }
    }

    private void executeBuyOrder(Order newOrder, Request request) {
        Iterator<Order> iterator = sellOrders.iterator();

        while (iterator.hasNext() && newOrder.getQuantity() > 0) {
            Order existingOrder = iterator.next();

            if (canExecute(newOrder, existingOrder)) {
                // Рассчитываем минимальное количество в долларах
                double executedQuantity = Math.min(
                        newOrder.getQuantity(),
                        existingOrder.getQuantity() / existingOrder.getPrice()
                );

                // Рассчитываем общую стоимость в рублях
                double executedCost = executedQuantity * existingOrder.getPrice();

                // Уменьшаем остатки
                newOrder.setQuantity(newOrder.getQuantity() - executedQuantity);
                existingOrder.setQuantity(existingOrder.getQuantity() - executedCost);

                // Обновляем статус продающего ордера
                if (existingOrder.getQuantity() <= 0) {
                    existingOrder.setStatus(Status.FULL_SUCCESS);
                    completedOrders.add(existingOrder);
                    iterator.remove();
                } else {
                    existingOrder.setStatus(Status.PARTIAL_SUCCESS);
                }

                // Обновляем статус покупающего ордера
                if (newOrder.getQuantity() <= 0) {
                    newOrder.setStatus(Status.FULL_SUCCESS);
                    request.getFuture().complete(new Callback(Status.FULL_SUCCESS, newOrder));
                    return;
                }
            }
        }

        // Если ордер на покупку остался частично исполненным
        if (newOrder.getQuantity() > 0) {
            newOrder.setStatus(Status.PARTIAL_SUCCESS);
            request.getFuture().complete(new Callback(Status.PARTIAL_SUCCESS, newOrder));
        }
    }

    // Логика для обработки ордеров на продажу
    private void executeSellOrder(Order newOrder, Request request) {
        Iterator<Order> iterator = buyOrders.iterator();

        while (iterator.hasNext() && newOrder.getQuantity() > 0) {
            Order existingOrder = iterator.next();

            if (canExecute(newOrder, existingOrder)) {
                // Рассчитываем минимальное количество в рублях
                double executedCost = Math.min(
                        newOrder.getQuantity() / newOrder.getPrice(),
                        existingOrder.getQuantity()
                );

                // Рассчитываем количество долларов
                double executedQuantity = executedCost * newOrder.getPrice();

                // Уменьшаем остатки
                newOrder.setQuantity(newOrder.getQuantity() - executedQuantity);
                existingOrder.setQuantity(existingOrder.getQuantity() - executedCost);

                // Обновляем статус покупающего ордера
                if (existingOrder.getQuantity() <= 0) {
                    existingOrder.setStatus(Status.FULL_SUCCESS);
                    completedOrders.add(existingOrder);
                    iterator.remove();
                } else {
                    existingOrder.setStatus(Status.PARTIAL_SUCCESS);
                }

                // Обновляем статус продающего ордера
                if (newOrder.getQuantity() <= 0) {
                    newOrder.setStatus(Status.FULL_SUCCESS);
                    request.getFuture().complete(new Callback(Status.FULL_SUCCESS, newOrder));
                    return;
                }
            }
        }

        // Если ордер на продажу остался частично исполненным
        if (newOrder.getQuantity() > 0) {
            newOrder.setStatus(Status.PARTIAL_SUCCESS);
            request.getFuture().complete(new Callback(Status.PARTIAL_SUCCESS, newOrder));
        }
    }





    private boolean canExecute(Order newOrder, Order existingOrder) {
        // Условие самостоятельной торговли
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


    public void printOrderBook() {
        System.out.println("Buy Orders:");
        buyOrders.forEach(System.out::println);

        System.out.println("Sell Orders:");
        sellOrders.forEach(System.out::println);
    }
}
