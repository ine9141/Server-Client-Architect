package server.core.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class SetupHandler {
    private static SetupHandler instance = new SetupHandler();

    private HashMap<Integer, Socket> clients;
    private HashMap<Socket, ObjectOutputStream> outputStreams;
    private HashMap<Socket, ObjectInputStream> inputStreams;

    private SetupHandler() {
        clients = new HashMap<>();
        outputStreams = new HashMap<>();
        inputStreams = new HashMap<>();
    }

    public static SetupHandler getInstance() {
        return instance;
    }

    public void setClients(int num, Socket socket) throws IOException {
        clients.put(num, socket);
        outputStreams.put(socket, new ObjectOutputStream(socket.getOutputStream()));
        inputStreams.put(socket, new ObjectInputStream(socket.getInputStream()));
    }

    public HashMap<Integer, Socket> getClients() {
        return clients;
    }

    public HashMap<Socket, ObjectInputStream> getInputStreams() {
        return inputStreams;
    }

    public HashMap<Socket, ObjectOutputStream> getOutputStreams() {
        return outputStreams;
    }
}