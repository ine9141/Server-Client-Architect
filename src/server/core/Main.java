package server.core;

import server.core.handler.ClientHandler;
import server.core.handler.MatrixHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Main { //row col calc calc
    public static void main(String[] args) throws IOException {

        MatrixHandler matrixHandler = new MatrixHandler();
        HashMap<Integer,Socket> clients = new HashMap<>();

        ServerSocket serverSocket = new ServerSocket(23921);
        System.out.println("[Server] 서버 시작.");

        for(int i = 0; i < 4 ; i++) {
            Socket socket = null;
            while (socket == null) {
                socket = serverSocket.accept();
            }
            System.out.println("[Server] " + socket.getRemoteSocketAddress().toString() + " 클라이언트 연결 완료.");
            clients.put(i, socket);
        }
        ClientHandler clientHandlerThread = new ClientHandler(new MatrixHandler(), clients);
        clientHandlerThread.start();
    }

}
