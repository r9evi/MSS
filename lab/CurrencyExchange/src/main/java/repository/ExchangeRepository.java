package repository;

import callback.Callback;
import callback.Status;
import client.ClientService;
import currency.CurrencyPairs;
import order.Order;
import request.OrderInfoRequest;
import request.PlaceOrderRequest;
import request.Request;
import request.RequestQueue;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExchangeRepository {
    private final RequestQueue queue;

    private final CurrencyPairs currencyPairs;

    private final ExecutorService executor;



    private ExchangeRepository() {
        this.queue = RequestQueue.getInstance();
        executor = Executors.newSingleThreadExecutor();
        currencyPairs = new CurrencyPairs();
        startQueueProcessing();
    }

    private static final class InstanceHolder {
        private static final ExchangeRepository instance = new ExchangeRepository();
    }

    public static ExchangeRepository getInstance() {
        return InstanceHolder.instance;
    }

    private void startQueueProcessing() {
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Request request = queue.getQueue().take();
                    processRequestSafely(request);
                } catch (InterruptedException e) {
                   Thread.currentThread().interrupt();
                    denyService();
                    break;
                }
            }

        });
    }

    private void processRequestSafely(Request request) {
        switch (request.getType()) {
            case PLACE_ORDER -> processOrderPlacement((PlaceOrderRequest) request.getRequestData(), request);
            case GET_ORDER_INFO -> processOrderInfo((OrderInfoRequest) request.getRequestData(), request);
        }

    }

    public void processOrderPlacement(PlaceOrderRequest orderPlacementInfo, Request request) {
        int clientId = orderPlacementInfo.getClientId();
        Order order = new Order(clientId, orderPlacementInfo.getBase(),
                orderPlacementInfo.getTarget(), orderPlacementInfo.getOrderType(), orderPlacementInfo.getAmount(), orderPlacementInfo.getPrice());
        ClientService.addClientEvent(clientId, order.getFuture());
        currencyPairs.placeCurrencyPairOrder(order, request);
    }

    public void processOrderInfo(OrderInfoRequest orderInfo, Request request) {
        currencyPairs.getOrderInfo(orderInfo.getClientId(), orderInfo.getOrderId(),
                orderInfo.getBase(), orderInfo.getTarget(), orderInfo.getType(), request);
    }

    public void denyService() {
        Request element;
        while ((element = queue.getQueue().poll()) != null) {
           element.getFuture().complete(new Callback(Status.FAILURE, "Отказ в обслуживании. Биржа закрылась"));
        }
    }

//    public void denyRequest(Request request) {
//        request.getFuture().complete(new Callback(Status.PARTIAL_SUCCESS, "Отказ в обслуживании"));
//    }


    public void stopProcessingRequests() {
        //isRunning = false;  // Останавливаем основной цикл
        if (executor != null && !executor.isShutdown()) {
            try {
                // Попытка корректно завершить все задачи
                executor.shutdownNow();
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate in the specified time.");
                }
            } catch (InterruptedException e) {
                System.err.println("Shutdown interrupted");
                Thread.currentThread().interrupt();
            } finally {
                denyService();
                queue.clear();
            }
        }
    }
}

