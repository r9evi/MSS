package response;

import callback.Callback;

import java.util.concurrent.CompletableFuture;

public class Response {
    private final String message;
    //private final CompletableFuture<Callback> future;


    public Response(String message/*, CompletableFuture<Callback> future*/) {
        this.message = message;
        //this.future = future;
    }

    public String getMessage() {
        return message;
    }

//    public CompletableFuture<Callback> getFuture() {
//        return future;
//    }
}
