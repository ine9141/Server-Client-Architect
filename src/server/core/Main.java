package server.core;
import server.core.handler.ClientHandler;
import server.core.handler.MatrixHandler;
import server.core.handler.StreamHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

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
                ClientList.getIntegerToSocket().put(ClientList.getClientSize(), socket);
                ClientList.addClient(socket);
                System.out.println("[Server] " + socket.getRemoteSocketAddress().toString() + " 클라이언트 연결 완료.");

                // StreamHandler에 해당 소켓의 ObjectOutputStream, ObjectInputStream 등록
                Map<Socket, ObjectOutputStream> outputStreamMap = StreamHandler.getOutputStreamMap();
                Map<Socket, ObjectInputStream> inputStreamMap = StreamHandler.getInputStreamMap();
                outputStreamMap.put(socket, new ObjectOutputStream(socket.getOutputStream()));
                inputStreamMap.put(socket, new ObjectInputStream(socket.getInputStream()));

                if(ClientList.getClientSize()==4) {
                    System.out.println("4명 모였다.");
                    int size = ClientList.getIntegerToSocket().size();
                    System.out.println("size = " + size);
                    break;
                }
//                ClientHandler clientHandler = new ClientHandler(socket, serverSocket);
//                Thread thread = new Thread(clientHandler);
//                thread.start();
            }

            for(int i=0; i<2; i++) { // 일단 0 1만 테스트중
                MatrixHandler matrixHandler = new MatrixHandler(serverSocket, i);
                Thread thread = new Thread(matrixHandler);
                thread.start();
            }

            while(true) {

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
