package request;

import callback.Callback;

import java.util.concurrent.CompletableFuture;

public class Request {
    private final RequestType type;
    private final int clientId;
    private final Object requestData;

    private final CompletableFuture<Callback> future;

    public Request(RequestType type, int providerId, Object requestData) {
        this.type = type;
        this.clientId = providerId;
        this.requestData = requestData;
        this.future = new CompletableFuture<>();

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

    public CompletableFuture<Callback> getFuture() {
        return future;
    }
}
