package repository;

import callback.Callback;
import callback.Status;
import client.ClientService;
import currency.CurrencyPairs;
import order.Order;
import request.*;


import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExchangeRepository {
   // private final RequestDisruptor disruptor;
    //private final CurrencyPairs currencyPairs;
    //private final ExecutorService executor;

    //private volatile static boolean isRunning = true;


    private ExchangeRepository() {
        //this.disruptor = RequestDisruptor.getInstance();
        //executor = Executors.newSingleThreadExecutor();
       // currencyPairs = new CurrencyPairs();
        //startQueueProcessing();
    }

    private static final class InstanceHolder {
        private static final ExchangeRepository instance = new ExchangeRepository();
    }

    public static ExchangeRepository getInstance() {
        return InstanceHolder.instance;
    }



//    private void processRequestSafely(Request request) {
//        if (Objects.requireNonNull(request.getType()) == RequestType.PLACE_ORDER) {
//            processOrderPlacement((PlaceOrderRequest) request.getRequestData(), request);
//        }
//    }

//    public void processOrderPlacement(PlaceOrderRequest orderPlacementInfo, Request request) {
//        int clientId = orderPlacementInfo.getClientId();
//        Order order = new Order(clientId, orderPlacementInfo.getBase(),
//                orderPlacementInfo.getTarget(), orderPlacementInfo.getOrderType(), orderPlacementInfo.getAmount(), orderPlacementInfo.getPrice());
//        ClientService.addClientEvent(clientId, order.getFuture());
//        currencyPairs.placeCurrencyPairOrder(order, request);
//    }


//    public void denyService() {
//        Request element;
//        while ((element = disruptor.getDisruptor().poll()) != null) {
//           element.getFuture().complete(new Callback(Status.FAILURE, "Отказ в обслуживании. Биржа закрылась"));
//        }
//    }

//    public void stopProcessingRequests() {
//        isRunning = false;
//        if (executor != null && !executor.isShutdown()) {
//            try {
//                executor.shutdownNow();
//                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
//                    System.err.println("Executor did not terminate in the specified time.");
//                }
//            } catch (InterruptedException e) {
//                System.err.println("Shutdown interrupted");
//                Thread.currentThread().interrupt();
//            } finally {
//                denyService();
//                disruptor.clear();
//            }
//        }
//    }
}

