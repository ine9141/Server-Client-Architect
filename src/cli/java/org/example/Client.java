package cli.java.org.example;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    public static final int MAX_row = 10;
    public static final int MAX_column = 10;

    public void reset(int[][] matrix){
        for(int row = 0 ; row < MAX_row ; row++) {
            for(int col = 0 ; col < MAX_column ; col++) {
                matrix[row][col] = (int)(Math.random()*100);
            }
        }
        System.out.println("matrix reset clear");
    }

    public int calc(int[] sended_row, int[] sended_col){
        int answer = 0;

        for (int index = 0; index < 10; index++) {
            answer += sended_row[index] * sended_col[index];
        }
        return answer;
    }

    public int[] array(String mode, int[][] matrix, int line){

        int[] send_array = new int[10];

        if (mode.equals("row")) for (int row = 0; row < MAX_row; row++) send_array[row] = matrix[line][row];
        else if (mode.equals("col")) for (int col = 0; col < MAX_column; col++) send_array[col] = matrix[col][line];

        return send_array;
    }

    public void start() throws IOException {
        Socket socket = null;
        try {
            Scanner scanner = new Scanner(System.in);
            int[][] matrix = new int[10][10];
            int sys_clock = 0;
            String mode = null;
            socket = new Socket("localhost", 23921);
            System.out.println("[서버에 접속 성공]");

            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

            while (true) {

                try{
                    mode = objectInput.readObject().toString();
                } catch(EOFException e){
                    continue;
                }

                //new round start
                //1. 배열 초기화
                if (mode.equals("0")) reset(matrix);

                else if (mode.equals("1")) {
                    Integer[] row_check = (Integer[]) objectInput.readObject();
                    int line = (int) Math.floor(Math.random() * row_check.length);
                    int[] send_array = array("row", matrix, line);

                    objectOutput.writeObject(line);
                    objectOutput.writeObject(send_array);

                    System.out.println("line(row) : " + line);
                    System.out.println("array : " + Arrays.toString(send_array));
                }
                else if(mode.equals("2")){
                    Integer[] col_check = (Integer[]) objectInput.readObject();
                    int line = (int) Math.floor(Math.random() * col_check.length);
                    int[] send_array = array("col", matrix, line);

                    objectOutput.writeObject(line);
                    objectOutput.writeObject(send_array);

                    System.out.println("line(col) : " + line);
                    System.out.println("array : " + Arrays.toString(send_array));
                }

                else if (mode.equals("3")) {
                    int answer = calc((int[]) objectInput.readObject(), (int[]) objectInput.readObject());
                    objectOutput.writeObject(answer);
                    System.out.println("answer : "+ answer );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            Objects.requireNonNull(socket).close();
        }
    }

    public static void main(String[] args) throws IOException {
        Client hahaClient = new Client();
        hahaClient.start();
    }
}