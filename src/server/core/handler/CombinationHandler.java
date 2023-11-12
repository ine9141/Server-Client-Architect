package server.core.handler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

public class CombinationHandler implements Runnable{
    private ObjectInputStream objectInputStream0;
    private ObjectInputStream objectInputStream1;
    private ObjectInputStream objectInputStream2;
    private ObjectInputStream objectInputStream3;
    private ObjectOutputStream objectOutputStream0;
    private ObjectOutputStream objectOutputStream1;
    private ObjectOutputStream objectOutputStream2;
    private ObjectOutputStream objectOutputStream3;
    private int round;
    private int c;


    public CombinationHandler(ObjectOutputStream objectOutputStream0, ObjectOutputStream objectOutputStream1, ObjectOutputStream objectOutputStream2, ObjectOutputStream objectOutputStream3,
                              ObjectInputStream objectInputStream0, ObjectInputStream objectInputStream1, ObjectInputStream objectInputStream2, ObjectInputStream objectInputStream3,
                              int round, int c) {

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
    }
    @Override
    public void run() {
        Thread thread0 = new Thread(new ClientHandler(0, round, c, objectInputStream0, objectOutputStream0));
        Thread thread1 = new Thread(new ClientHandler(1, round, c, objectInputStream1, objectOutputStream1));
        Thread thread2 = new Thread(new ClientHandler(2, round, c, objectInputStream2, objectOutputStream2));
        Thread thread3 = new Thread(new ClientHandler(3, round, c, objectInputStream3, objectOutputStream3));
        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
