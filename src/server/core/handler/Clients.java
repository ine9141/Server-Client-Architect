package server.core.handler;

import java.net.Socket;

public class Clients {
    private static Socket[] clients = new Socket[4];
    private static int clientSize = 0;

    public void addClient(Socket socket) {
        clients[clientSize++] = socket;
    }

    public static Socket[] getClients() {
        return clients;
    }

    public static int getClientSize() {
        return clientSize;
    }
}
