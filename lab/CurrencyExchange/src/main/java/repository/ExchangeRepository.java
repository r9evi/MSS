package repository;

import callback.Callback;
import callback.Status;
import request.OrderInfoRequest;
import request.PlaceOrderRequest;
import request.Request;
import request.RequestQueue;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangeRepository {
    private static ExchangeRepository instance;
    private final RequestQueue queue;

    private final ExecutorService executor;

    private ExchangeRepository() {
        this.queue = RequestQueue.getInstance();
        executor = Executors.newSingleThreadExecutor();
        startQueueProcessing();
    }

    public static ExchangeRepository getInstance() {
        if (instance == null) {
            instance = new ExchangeRepository();
        }
        return instance;
    }

    private void startQueueProcessing() {
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Request request = queue.getQueue().take();
                    switch (request.getType()) {
                        case PLACE_ORDER -> processOrderPlacement((PlaceOrderRequest) request.getRequestData(), request);
                        case GET_ORDER_INFO -> processOrderInfo((OrderInfoRequest) request.getRequestData(), request);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    denyService();
                    break;
                }
            }
        });
    }

    public void processOrderPlacement(PlaceOrderRequest orderPlacementInfo, Request request) {

    }

    public void processOrderInfo(OrderInfoRequest orderInfo, Request request) {

    }

    public void denyService() {
        Request element;
        while ((element = queue.getQueue().poll()) != null) {
           element.getFuture().complete(new Callback(Status.FAILURE, "Отказ в обслуживании. Биржа закрылась"));
        }
    }


    public void stopProcessingRequests() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();  // Прерываем выполнение потока
        }
    }
}

