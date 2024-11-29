package request;

import com.lmax.disruptor.EventFactory;

public class RequestEventFactory implements EventFactory<Request> {
    @Override
    public Request newInstance() {
        return new Request();
    }
}
