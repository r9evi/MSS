package request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestQueue {
    private final BlockingQueue<Request> queue;

    private RequestQueue() {
        queue = new LinkedBlockingQueue<>();
    }

    private static final class InstanceHolder {
        private static final RequestQueue instance = new RequestQueue();
    }

    public static RequestQueue getInstance() {
        return RequestQueue.InstanceHolder.instance;
    }

    public BlockingQueue<Request> getQueue() {
        return queue;
    }

    public void clear() {
        queue.clear();
    }
}
