package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public void start() throws IOException {
        Socket socket = null;
        try {
            Scanner scanner = new Scanner(System.in);
            socket = new Socket("localhost", 23921);
            System.out.println("[서버에 접속 성공]");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) {

                // 클라이언트에서 서버로 메시지 보내기
                System.out.print("보낼 메시지를 입력하세요 :");
                String outputMessage = scanner.nextLine();
                bufferedWriter.write(outputMessage);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                // 서버로부터 응답을 받습니다.
                System.out.println("메시지 받기 대기중...");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String inputMessage = reader.readLine(); // 서버에서의 응답을 읽음
                if(inputMessage!=null) {
                    System.out.println("서버로부터 받은 메시지 = " + inputMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Client hahaClient = new Client();
        hahaClient.start();
    }
}
