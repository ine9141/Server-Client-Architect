package server.core;
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

                clientIP = socket.getRemoteSocketAddress().toString();

                while(true) {
                    // 메시지 받기
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String receiveMsg = reader.readLine();
                    if(receiveMsg != null)
                        System.out.println("receiveMsg = " + receiveMsg);

                    // 메시지 보내기
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String message = "안녕하세요, 서버입니다.";
                    writer.write(message);
                    writer.newLine();
                    writer.flush();
                }

                //writer.close();
                //reader.close();
                //socket.close();
                //serverSocket.close();

            } catch (IOException e) {

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
