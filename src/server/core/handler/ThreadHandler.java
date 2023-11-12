package server.core.handler;

import java.io.IOException;
import java.net.Socket;

public class ThreadHandler implements Runnable {
    private Socket socket;
    private int num;

    public ThreadHandler(int num, Socket socket) {
        this.num = num;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            SetupHandler setupHandler = SetupHandler.getInstance();
            setupHandler.setClients(num, socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}