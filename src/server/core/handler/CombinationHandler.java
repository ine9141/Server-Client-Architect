package server.core.handler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CombinationHandler implements Runnable{
    private Socket socket0;
    private Socket socket1;
    private Socket socket2;
    private Socket socket3;
    private ServerSocket serverSocket;
    private int round;
    private int c;

    private MatrixHandler matrixHandler;


    public CombinationHandler(Socket socket0, Socket socket1, Socket socket2, Socket socket3, ServerSocket serverSocket, int round, int c, MatrixHandler matrixHandler) {
        this.serverSocket = serverSocket;
        this.socket0 = socket0;
        this.socket1 = socket1;
        this.socket2 = socket2;
        this.socket3 = socket3;
        this.round = round;
        this.c = c;
    }

    @Override
    public void run() {
        Thread thread0 = new Thread(new ClientHandler(socket0, serverSocket, 0, round, c, matrixHandler));
        Thread thread1 = new Thread(new ClientHandler(socket1, serverSocket, 1, round, c, matrixHandler));
        Thread thread2 = new Thread(new ClientHandler(socket2, serverSocket, 2, round, c, matrixHandler));
        Thread thread3 = new Thread(new ClientHandler(socket3, serverSocket, 3, round, c, matrixHandler));
        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
