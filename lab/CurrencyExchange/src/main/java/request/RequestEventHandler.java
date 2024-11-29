package request;

import com.lmax.disruptor.EventHandler;
import currency.CurrencyPairs;
import order.Order;

public class RequestEventHandler implements EventHandler<Request> {
    private final CurrencyPairs currencyPairs = new CurrencyPairs(); // Для обработки ордеров

    @Override
    public void onEvent(Request request, long sequence, boolean endOfBatch) throws Exception {
        // Обработка запроса
        if (request.getType() == RequestType.PLACE_ORDER) {
            PlaceOrderRequest orderRequest = (PlaceOrderRequest) request.getRequestData();
            processOrderPlacement(orderRequest, request);
        }

        // Можно добавлять дополнительные проверки и обработку для других типов запросов, если это необходимо
    }

    private void processOrderPlacement(PlaceOrderRequest orderPlacementInfo, Request request) {
        int clientId = orderPlacementInfo.getClientId();
        Order order = new Order(clientId, orderPlacementInfo.getBase(),
                orderPlacementInfo.getTarget(), orderPlacementInfo.getOrderType(), orderPlacementInfo.getAmount(), orderPlacementInfo.getPrice());

        // Отправка события клиенту
        //ClientService.addClientEvent(clientId, order.getFuture());

        // Размещение ордера на валютной паре
        currencyPairs.placeCurrencyPairOrder(order, request);
    }
}
