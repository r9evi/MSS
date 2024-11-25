package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientService {
    private final List<Client> clients;

    private static ClientService instance;

    private ClientService() {
        this.clients = new ArrayList<>();
    }

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public List<Client> getClients() {
        return clients;
    }

    public Client getClient(int clientId) {
        return clients.get(clientId);
    }

    public void addClient(Client client) {
        if (!clients.contains(client)) {
            clients.add(client);
        }
    }

    public void addClients(Client[] clients) {
        this.clients.addAll(Arrays.asList(clients));
    }
}
