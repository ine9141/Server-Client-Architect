package server.core.handler;

import server.core.Instruction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable {

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
                objectOutput.writeObject(new Instruction(true,false,false, false)); //새 라운드 시작

                //차례대로 행 입력
                objectOutput.writeObject(new Instruction(false,true,true, false));
                int line1 = (int) objectInput.readObject();
                int[] matrix1 = (int[]) objectInput.readObject();

                //열 입력
                objectOutput.writeObject(new Instruction(false, true, false, false));
                int line2 = (int) objectInput.readObject();
                int[] matrix2 = (int[]) objectInput.readObject();

                objectOutput.writeObject(new Instruction(false,false,false, true));
                objectOutput.writeObject(matrix1);
                objectOutput.writeObject(matrix2);

                int answer = (int) objectInput.readObject();

                matrix[line1][line2] = answer;

                System.out.println(matrix[line1][line2]);
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
