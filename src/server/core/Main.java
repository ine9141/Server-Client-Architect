package server.core;

import server.core.handler.ClientHandler;
import server.core.handler.CombinationHandler;
import server.core.handler.MatrixHandler;
import server.core.handler.TransmissionHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        new MatrixHandler();
        new TransmissionHandler();
        Socket[] clients = new Socket[4];
        ObjectInputStream[] objectInputStreams = new ObjectInputStream[4];
        ObjectOutputStream[] objectOutputStreams = new ObjectOutputStream[4];
        int[][] comb = {{0, 1, 2, 3}, {0, 2, 1, 3}, {0, 3, 1, 2}, {1, 2, 0, 3}, {1, 3, 0, 2}, {2, 3, 0, 1}};

        ServerSocket serverSocket = new ServerSocket(23921);
        System.out.println("[Server] 서버 시작.");

        for(int i = 0; i < 4 ; i++) {
            Socket socket = null;
            while (socket == null) {
                socket = serverSocket.accept();
            }
            System.out.println("[Server] " + socket.getRemoteSocketAddress().toString() + " 클라이언트 연결 완료.");
            clients[i] = socket;
            objectOutputStreams[i] = new ObjectOutputStream(socket.getOutputStream());
            objectInputStreams[i] = new ObjectInputStream(socket.getInputStream());
        }

        for(int round = 0; round < 100; round++){
            while(true){
                int c = (int)(Math.random()*6);
                if(MatrixHandler.getMatLen(round,c) == 100) {
                    c = MatrixHandler.findNextComb(round,c);
                    if (c == -1) break;
                }
                new Thread(new CombinationHandler(
                        objectOutputStreams[comb[c][0]], objectOutputStreams[comb[c][1]], objectOutputStreams[comb[c][2]], objectOutputStreams[comb[c][3]],
                        objectInputStreams[comb[c][0]], objectInputStreams[comb[c][1]], objectInputStreams[comb[c][2]], objectInputStreams[comb[c][3]],
                        round, c)).start();
                while(!MatrixHandler.matrixFlag){
                    Thread.sleep(50);
                }
                MatrixHandler.matrixFlag = false;
                MatrixHandler.updateNextRoundFlag(round);
                if (MatrixHandler.nextRoundFlag) break;
            }
        }
    }
}
