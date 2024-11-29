package request;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import order.Order;
import service.ExchangeService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestDisruptor {
    private static final int BUFFER_SIZE = 1024;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final RequestEventFactory factory = new RequestEventFactory();

    private final Disruptor<Request> disruptor;

    private RequestDisruptor() {
        disruptor = new Disruptor<>(factory, BUFFER_SIZE, executor, ProducerType.SINGLE, new BlockingWaitStrategy());
        disruptor.handleEventsWith(new RequestEventHandler());
        disruptor.start();
    }

    private static final class InstanceHolder {
        private static final RequestDisruptor instance = new RequestDisruptor();
    }

    public static RequestDisruptor getInstance() {
        return RequestDisruptor.InstanceHolder.instance;
    }

    public Disruptor<Request> getDisruptor() {
        return disruptor;
    }

    public void shutdown() {
        disruptor.shutdown();
        executor.shutdown();
    }


}
