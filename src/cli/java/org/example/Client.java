package cli.java.org.example;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    public static final int MAX_row = 10;
    public static final int MAX_column = 10;


    public void start() throws IOException {
        Socket socket = null;
        try {
            Scanner scanner = new Scanner(System.in);
            int[][] matrix = new int[10][10];
            int answer = 0;
            int sys_clock = 0;
            String mode = null;
            socket = new Socket("localhost", 23921);
            System.out.println("[서버에 접속 성공]");

            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

            while (true) {

                mode = (String) objectInput.readObject();
                //new round start
                //1. 배열 초기화
                if(mode.equals("new round")){
                    for(int row = 0 ; row < MAX_row ; row++) {
                        for(int col = 0 ; col < MAX_column ; col++) {
                            matrix[row][col] = (int)(Math.random()*100);
                        }
                    }
                    System.out.println("matrix reset clear");
                }


                //role setting
                //if role is matrix calc
                else if (mode.equals("calc")) {
                    int[] sended_row = new int[10];
                    int[] sended_col = new int[10];
                    boolean row_flag = false, col_flag = false;
                    while (true){
                        String mat_num = (String) objectInput.readObject();
                        if (Objects.equals(mat_num, "row")) {
                            sended_row = (int[]) objectInput.readObject();
                            row_flag = true;
                        }
                        else if (Objects.equals(mat_num, "cal")) {
                            sended_col = (int[]) objectInput.readObject();
                            col_flag = true;
                        }
                        else break;

                        if (row_flag && col_flag) break;
                    }

                    for (int row = 0; row < MAX_row; row++) {
                        for (int col = 0; col < MAX_column; col++) {
                            answer += sended_row[row] * sended_col[col];
                        }
                    }

                    objectOutput.writeObject(answer);
                }

                //role setting
                //if role is matrix transmission
                else if(mode.equals("matrix1") || mode.equals("matrix2")){
                    int[] send_array = new int[10];
                    int line = (int)(Math.random()*10);

                    if (mode.equals("matrix1")) {
                        mode = "row";
                        for (int row = 0; row < MAX_row; row++) send_array[row] = matrix[line][row];
                    }

                    else {
                        mode = "col";
                        for (int col = 0; col < MAX_column; col++) send_array[col] = matrix[col][line];
                    }

                    objectOutput.writeObject(mode);
                    objectOutput.writeObject(line);
                    objectOutput.writeObject(send_array);
                    System.out.println(mode);
                    System.out.println(line);
                    System.out.println(Arrays.toString(send_array));


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Client hahaClient = new Client();
        hahaClient.start();
    }
}