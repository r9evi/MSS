package order;

import callback.Callback;
import callback.Status;
import request.Request;

import java.util.*;


public class OrderBook {
    private final PriorityQueue<Order> buyOrders;
    private final PriorityQueue<Order> sellOrders;

    public OrderBook() {
        buyOrders = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice).reversed());
        sellOrders = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice));
    }

//    public void getInfo(int clientId, int orderId, OrderType type, Request request) {
//        if (type.equals(OrderType.BUY)) {
//            findOrderByInfo(clientId, orderId, buyOrders, request);
//        } else {
//            findOrderByInfo(clientId, orderId, sellOrders, request);
//        }
//    }

//    public void findOrderByInfo(int clientId, int orderId, Queue<Order> orders, Request request) {
//        for (Order order : orders) {
//            if (order.getOrderId() == clientId && order.getOrderId() == orderId) {
//                request.getFuture().complete(new Callback(order.getStatus(), order));
//            } else {
//                request.getFuture().complete(new Callback(Status.FAILURE, "Ордер не найден"));
//            }
//        }
//    }

    public void placeOrder(Order newOrder, Request request) {
        if (newOrder.getOrderType() == OrderType.BUY) {
            if (sellOrders.isEmpty()) {
                buyOrders.offer(newOrder);
                request.setCallback(new Callback(Status.CREATED, "Ордер размещен"));
            } else {
                processOrder(newOrder, request, sellOrders);
            }
        } else {
            if (buyOrders.isEmpty()) {
                sellOrders.offer(newOrder);
                request.setCallback(new Callback(Status.CREATED, "Ордер размещен"));
            } else {
                processOrder(newOrder, request, buyOrders);
            }
        }
    }

    private void processOrder(Order newOrder, Request request, Queue<Order> oppositeOrders) {
        Iterator<Order> iterator = oppositeOrders.iterator();
        while (iterator.hasNext() && newOrder.getQuantity() > 0) {
            Order existingOrder = iterator.next();
            if (canExecute(newOrder, existingOrder)) {
                double quantity = Math.min(newOrder.getQuantity(), existingOrder.getQuantity());
                newOrder.setQuantity(newOrder.getQuantity() - quantity);
                existingOrder.setQuantity(existingOrder.getQuantity() - quantity);
                if (existingOrder.getQuantity() <= 0) {
                    existingOrder.setStatus(Status.FULL_SUCCESS);
                    existingOrder.getFuture().complete(new Callback(existingOrder.getStatus(), existingOrder));
                    iterator.remove();
                } else {
                    existingOrder.setStatus(Status.PARTIAL_SUCCESS);
                }
                if (newOrder.getQuantity() <= 0) {
                    newOrder.setStatus(Status.FULL_SUCCESS);
                    request.setCallback(new Callback(Status.FULL_SUCCESS, newOrder));
                    request.getCallback().accept(request.getCallback());
                } else {
                    Order oldOrderCopy = Order.copy(newOrder);
                    newOrder.setInitialQuantity(newOrder.getQuantity());
                    if (newOrder.getOrderType().equals(OrderType.SELL)) {
                       sellOrders.add(newOrder);
                    } else {
                        buyOrders.add(newOrder);
                    }
                    request.setCallback(new Callback(Status.PARTIAL_SUCCESS, oldOrderCopy));
                    request.getCallback().accept(request.getCallback());
                }
                return;
            }
        }
        newOrder.setStatus(Status.CREATED);
        request.setCallback(new Callback(Status.CREATED, "Не найдено встречных. Размещен"));
        request.getCallback().accept(request.getCallback());
    }

    private boolean canExecute(Order newOrder, Order existingOrder) {
        if (newOrder.getClientId() == existingOrder.getClientId()) {
            return false;
        }
        if (newOrder.getOrderType() == OrderType.BUY) {
            return newOrder.getPrice() >= existingOrder.getPrice();
        } else if (newOrder.getOrderType() == OrderType.SELL) {
            return newOrder.getPrice() <= existingOrder.getPrice();
        }
        return false;
    }

}
