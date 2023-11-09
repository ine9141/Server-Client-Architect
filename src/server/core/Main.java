package server.core;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

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

    class ClientHandler implements Runnable {

        private Socket socket;

        private ServerSocket serverSocket;

        public ClientHandler(Socket socket, ServerSocket serverSocket) {
            this.socket = socket;
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            String clientIP = null;
            try {
                int[][] matrix = new int[10][10];

                clientIP = socket.getRemoteSocketAddress().toString();
                ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

                while(true) {
                    objectOutput.writeObject("new round"); //이게 클라이언트한테 명령하는 느낌


                    objectOutput.writeObject("matrix1");
                    String mode1 = (String) objectInput.readObject();
                    int line1 = (int) objectInput.readObject();
                    int[] matrix1 = (int[]) objectInput.readObject();


//                    objectOutput.writeObject("matrix2");
//                    String mode2 = (String) objectInput.readObject();
//                    int line2 = (int) objectInput.readObject();
//                    int[] matrix2 = (int[]) objectInput.readObject();
//
//                    objectOutput.writeObject("calc");
//
//                    objectOutput.writeObject("row");
//                    objectOutput.writeObject(matrix1);
//
//                    objectOutput.writeObject("cal");
//                    objectOutput.writeObject(matrix2);
//
//                    int answer = (int) objectInput.readObject();
//
//                    matrix[line1][line2] = answer;
//
//                    System.out.println(matrix[line1][line2]);
                    break;

                }

            } catch (IOException e) {

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    serverSocket.close();
                    socket.close(); // 해당 클라이언트와의 소켓 닫기
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
