package callback;

import java.util.function.Consumer;

public class Callback implements Consumer<Object> {
    private final Status status;
    private final Object result;

    public Callback(Status type, Object result) {
        this.status = type;
        this.result = result;
    }

    public Callback() {
        status = null;
        result = null;
    }

    public Status getStatus() {
        return status;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Callback{" +
                "status=" + status +
                ", result=" + result +
                '}';
    }

    @Override
    public void accept(Object o) {
        System.out.printf("Status: %s\n Result: %s\n", status, result);
    }
}
