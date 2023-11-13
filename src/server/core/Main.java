package server.core;

import server.core.handler.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main { //row col calc calc
    public static void main(String[] args) throws IOException, InterruptedException {

        MatrixHandler matrixHandler = new MatrixHandler();
        SetupHandler setupHandler = SetupHandler.getInstance();

        ServerSocket serverSocket = new ServerSocket(23921);
        System.out.println("[Server] 서버 시작.");

        LogHandler.initFile();

        for(int i = 0; i < 4 ; i++) {
            Socket socket = null;
            while (socket == null) {
                socket = serverSocket.accept();
            }
            System.out.println("[Server] " + socket.getRemoteSocketAddress().toString() + " 클라이언트 연결 완료.");
            ThreadHandler threadHandler = new ThreadHandler(i,socket);
            threadHandler.run();
            Thread.sleep(50);
            ClientHandler clientHandlerThread = new ClientHandler(new MatrixHandler(), i);
            clientHandlerThread.start();
            Thread.sleep(50);
        }
    }

}
