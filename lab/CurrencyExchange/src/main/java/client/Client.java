package client;

public class Client {
    private static int clientsId = 0;

    private int id;
    private final Wallet wallet;

    public Client() {
        this.id = clientsId++;
        this.wallet = new Wallet();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", wallet=" + wallet +
                '}';
    }
}
