package server.core;
import server.core.handler.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Main hahaServer = new Main();
        hahaServer.start();


    }

    public void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(23921);
            System.out.println("[Server] 서버 시작.");

            while (true) {
                System.out.println("[Server] 클라이언트 연결 대기중...");
                Socket socket = serverSocket.accept();
                ClientList.addClient(socket);
                System.out.println("[Server] " + socket.getRemoteSocketAddress().toString() + " 클라이언트 연결 완료.");
                ClientHandler clientHandler = new ClientHandler(socket, serverSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (IOException e) {
            // e.printStackTrace();
        } finally {
            //종료 로직
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                    System.out.println("[Server] 서버 종료");
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }

        }

    }
}
