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

    private ObjectInputStream objectInputStream0;
    private ObjectInputStream objectInputStream1;
    private ObjectInputStream objectInputStream2;
    private ObjectInputStream objectInputStream3;
    private ObjectOutputStream objectOutputStream0;
    private ObjectOutputStream objectOutputStream1;
    private ObjectOutputStream objectOutputStream2;
    private ObjectOutputStream objectOutputStream3;


    private ServerSocket serverSocket;
    private int round;
    private int c;

    private MatrixHandler matrixHandler;


    public CombinationHandler(Socket socket0, Socket socket1, Socket socket2, Socket socket3,
                              ObjectOutputStream objectOutputStream0, ObjectOutputStream objectOutputStream1, ObjectOutputStream objectOutputStream2, ObjectOutputStream objectOutputStream3,
                              ObjectInputStream objectInputStream0, ObjectInputStream objectInputStream1, ObjectInputStream objectInputStream2, ObjectInputStream objectInputStream3,
                              ServerSocket serverSocket, int round, int c, MatrixHandler matrixHandler) {

        this.serverSocket = serverSocket;
        this.socket0 = socket0;
        this.socket1 = socket1;
        this.socket2 = socket2;
        this.socket3 = socket3;
        this.objectInputStream0 = objectInputStream0;
        this.objectInputStream1 = objectInputStream1;
        this.objectInputStream2 = objectInputStream2;
        this.objectInputStream3 = objectInputStream3;
        this.objectOutputStream0 = objectOutputStream0;
        this.objectOutputStream1 = objectOutputStream1;
        this.objectOutputStream2 = objectOutputStream2;
        this.objectOutputStream3 = objectOutputStream3;
        this.round = round;
        this.c = c;
        this.matrixHandler = matrixHandler;
    }

    @Override
    public void run() {
        Thread thread0 = new Thread(new ClientHandler(socket0, serverSocket, 0, round, c, matrixHandler,objectInputStream0, objectOutputStream0));
        Thread thread1 = new Thread(new ClientHandler(socket1, serverSocket, 1, round, c, matrixHandler,objectInputStream1, objectOutputStream1));
        Thread thread2 = new Thread(new ClientHandler(socket2, serverSocket, 2, round, c, matrixHandler,objectInputStream2, objectOutputStream2));
        Thread thread3 = new Thread(new ClientHandler(socket3, serverSocket, 3, round, c, matrixHandler,objectInputStream3, objectOutputStream3));
        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
