package callback;

public class Callback {
    private final Status status;
    private final Object result;

    public Callback(Status type, Object result) {
        this.status = type;
        this.result = result;
    }

    public Status getType() {
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
}
