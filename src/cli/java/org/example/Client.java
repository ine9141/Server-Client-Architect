package cli.java.org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
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
            String mode;
            String select_calc;
            socket = new Socket("localhost", 23921);
            System.out.println("[서버에 접속 성공]");

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

            while (true) {

                //new round start
                //1. 배열 초기화
                for(int row = 0 ; row < MAX_row ; row++) {
                    for(int col = 0 ; col < MAX_column ; col++) {
                        matrix[row][col] = (int)(Math.random()*100);
                    }
                }

                mode = reader.readLine();

                //role setting
                //if role is matrix calc
                if (mode.equals("calc")) {
                    int[] sended_row = new int[10];
                    int[] sended_col = new int[10];
                    boolean row_flag = false, col_flag = false;
                    while (true){
                        String mat_num = reader.readLine();
                        if (mat_num == "matrix1") {
                            sended_row = (int[]) objectInput.readObject();
                            row_flag = true;
                        }
                        else if (mat_num == "matrix2") {
                            sended_col = (int[]) objectInput.readObject();
                            col_flag = true;
                        }

                        if (row_flag && col_flag) break;
                    }

                    for (int row = 0; row < MAX_row; row++) {
                        for (int col = 0; col < MAX_column; col++) {
                            answer += sended_row[row] * sended_col[col];
                        }
                    }

                    bufferedWriter.write(Integer.toString(answer));
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }

                //if role is matrix transmission
                else{
                    int[] send_array = new int[10];
                    int line = (int)(Math.random()*10);
                    if (mode.equals("matrix1")) for (int row = 0; row < MAX_row; row++) send_array[row] = matrix[line][row];
                    else if (mode.equals("matrix2")) for (int col = 0; col < MAX_column; col++) send_array[col] = matrix[col][line];
                    if ((int)(Math.random()*10)<5) select_calc = "calc1";
                    else select_calc = "calc2";

                    bufferedWriter.write(select_calc);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    objectOutput.writeObject(send_array);
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


//                // 클라이언트에서 서버로 메시지 보내기
//                System.out.print("보낼 메시지를 입력하세요 :");
//                String outputMessage = scanner.nextLine();
//                bufferedWriter.write(outputMessage);
//                bufferedWriter.newLine();
//                bufferedWriter.flush();
//
//                // 서버로부터 응답을 받습니다.
//                System.out.println("메시지 받기 대기중...");
//                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String inputMessage = reader.readLine(); // 서버에서의 응답을 읽음