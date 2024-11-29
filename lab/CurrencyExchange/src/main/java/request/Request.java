package request;

import callback.Callback;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Request  {
    private RequestType type;
    private int clientId;
    private Object requestData;

    private Callback callback;

    public Request(RequestType type, int providerId, Object requestData) {
        this.type = type;
        this.clientId = providerId;
        this.requestData = requestData;
        this.callback = new Callback();
    }

    public Request() {
        type = RequestType.EMPTY;
        clientId = -1;
        requestData = new Object();
    }

    public void setRequest(Request request) {
        this.type = request.type;
        this.clientId = request.clientId;
        this.requestData = request.requestData;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    public RequestType getType() {
        return type;
    }

    public int getClientId() {
        return clientId;
    }

    public Object getRequestData() {
        return requestData;
    }

}
