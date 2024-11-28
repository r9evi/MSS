package service;

import api.ServiceAPI;
import order.OrderType;
import repository.ExchangeRepository;
import currency.Currency;
import request.*;
import response.Response;

public class ExchangeService implements ServiceAPI {
    private static ExchangeService instance;
    private final ExchangeRepository repository;
    private final RequestQueue queue;

    private ExchangeService() {
        this.queue = RequestQueue.getInstance();
        this.repository = ExchangeRepository.getInstance();
    }

    public static ExchangeService getInstance() {
        if (instance == null) {
            instance = new ExchangeService();
        }
        return instance;
    }


    @Override
    public Response placeOrder(int clientId, OrderType type, Currency base, Currency target, double amount, double price) {
        var orderRequest = new PlaceOrderRequest(clientId, type, base, target, amount, price);
        Request request = new Request(RequestType.PLACE_ORDER, clientId, orderRequest);
        queue.getQueue().add(request);
        return new Response(String.format("Ожидается размещение ордера. Клиент: %d ", clientId), request.getFuture());
    }


    @Override
    public Response getOrderInfo(int clientId, int orderId, Currency base, Currency target, OrderType type) {
        var orderInfoRequest = new OrderInfoRequest(clientId, orderId, base, target, type);
        Request request = new Request(RequestType.GET_ORDER_INFO, clientId, orderInfoRequest);
        queue.getQueue().add(request);
        return new Response(String.format("Ожидается информация по ордеру %d", orderId), request.getFuture());
    }

    @Override
    public void closeExchange() {
        repository.stopProcessingRequests();
    }
}
