package cli.java.org.example;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;

import static cli.java.org.example.ClientLogHandler.*;

public class Client extends Thread{

    public static final int MAX_row = 10;
    public static final int MAX_column = 10;

    public static void reset(int[][] matrix){
        for(int row = 0 ; row < MAX_row ; row++) {
            for(int col = 0 ; col < MAX_column ; col++) {
                matrix[row][col] = (int)(Math.random()*100);
            }
        }
        System.out.println("matrix reset clear");
    }

    public static int calc(int[] sended_row, int[] sended_col){
        int answer = 0;

        for (int index = 0; index < 10; index++) {
            answer += sended_row[index] * sended_col[index];
        }
        return answer;
    }

    public static int[] array(String mode, int[][] matrix, int line){

        int[] send_array = new int[10];

        if (mode.equals("row")) for (int row = 0; row < MAX_row; row++) send_array[row] = matrix[line][row];
        else if (mode.equals("col")) for (int col = 0; col < MAX_column; col++) send_array[col] = matrix[col][line];

        return send_array;
    }


    @Override
    public void run() {
        Socket socket = null;
        try {
            int[][] matrix = new int[10][10];
            reset(matrix);
            int mode;
            System.out.println("[서버 접속 대기중]");
            while (socket == null) {
                try {
                    socket = new Socket("localhost", 23921);
//                    socket = new Socket("152.67.198.30", 23921);
                } catch (ConnectException e) {
                }
            }
            System.out.println("[서버 접속 성공]");

            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

            int clientNumber = (int) objectInput.readObject();
            initFile(clientNumber);

            while (true) {
                try{
                    mode = (int) objectInput.readObject();
                } catch(Exception e){
                    continue;
                }

                //new round start
                //1. 배열 초기화
                if (mode == 0) {
                    reset(matrix);
                    clientLog(clientNumber,"새로운 행렬 생성\n");
                    for (int i = 0; i < 10; i++){
                        for (int j = 0; j < 10; j++){
                            clientLog(clientNumber, "[" + matrix[i][j] + "] ");
                        }
                        clientLog(clientNumber, "\n");
                    }
                }

                else if (mode == 1) {
                    int line = (int) objectInput.readObject();
                    int[] send_array = array("row", matrix, line);
                    objectOutput.writeObject(line);
                    objectOutput.writeObject(send_array);

                    clientLog(clientNumber, "Get Row 수행. Line :" + line + "\n");
                    System.out.println("line(row) : " + line);
                    System.out.println("array : " + Arrays.toString(send_array));

                } else if(mode == 2){
                    int line = (int) objectInput.readObject();
                    int[] send_array = array("col", matrix, line);

                    objectOutput.writeObject(line);
                    objectOutput.writeObject(send_array);

                    clientLog(clientNumber, "Get Column 수행. Line :" + line + "\n");
                    System.out.println("line(col) : " + line);
                    System.out.println("array : " + Arrays.toString(send_array));

                } else if (mode == 3) {
                    int answer = calc((int[]) objectInput.readObject(), (int[]) objectInput.readObject());
                    clientLog(clientNumber, "Calculation 수행 결과. : " + answer + "\n");
                    objectOutput.writeObject(answer);
                    System.out.println("answer : "+ answer );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
