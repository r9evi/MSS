package callback;

public enum Status {
    FULL_SUCCESS("Полностью выполнен"),
    PARTIAL_SUCCESS("Частично выполнен"),
    CREATED("Создан"),
    FAILURE("Ошибка");

    private final String status;
    Status(String s) {
        this.status = s;
    }

    public String getStatus() {
        return this.status;
    }
}
