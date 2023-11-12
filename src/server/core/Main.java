package server.core;

import server.core.handler.ClientHandler;
import server.core.handler.CombinationHandler;
import server.core.handler.MatrixHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public class Main {
    public static void main(String[] args) throws IOException {

        MatrixHandler matrixHandler = new MatrixHandler();
        HashMap<Integer,Socket> clients = new HashMap<>();
        HashMap<Integer,ObjectOutputStream> outputStreams = new HashMap<>();
        HashMap<Integer,ObjectInputStream> inputStreams = new HashMap<>();
        int[][] comb = {{0, 1, 2, 3}, {0, 2, 1, 3}, {0, 3, 1, 2}, {1, 2, 0, 3}, {1, 3, 0, 2}, {2, 3, 0, 1}};

        ServerSocket serverSocket = new ServerSocket(23921);
        System.out.println("[Server] 서버 시작.");

        for(int i = 0; i < 4 ; i++) {
            Socket socket = null;
            while (socket == null) {
                socket = serverSocket.accept();
            }
            System.out.println("[Server] " + socket.getRemoteSocketAddress().toString() + " 클라이언트 연결 완료.");
            clients.put(i, socket);
            outputStreams.put(i,new ObjectOutputStream(socket.getOutputStream()));
            inputStreams.put(i,new ObjectInputStream(socket.getInputStream()));
        }

        for(int round = 0; round < 1; round++){
            for(int c = 0 ; c < 2 ; c++){
                CombinationHandler combinationHandler = new CombinationHandler(
                        clients.get(comb[c][0]),clients.get(comb[c][1]),clients.get(comb[c][2]),clients.get(comb[c][3]),
                        outputStreams.get(comb[c][0]),outputStreams.get(comb[c][1]),outputStreams.get(comb[c][2]),outputStreams.get(comb[c][3]),
                        inputStreams.get(comb[c][0]),inputStreams.get(comb[c][1]),inputStreams.get(comb[c][2]),inputStreams.get(comb[c][3]),
                        serverSocket,round,c,matrixHandler
                );
                Thread thread = new Thread(combinationHandler);
            }
        }
    }

}
