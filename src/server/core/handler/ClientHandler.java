package server.core.handler;

import server.core.ClientList;
import server.core.Instruction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable {//소켓 접속 때 마다 하나 생김

    private Socket socket;
    private ServerSocket serverSocket;
    private int targetMatriexIndex; // 0~5를 가짐. 0이면 AB가 Mat CD가 calc임을 알 수 있음.

    private static int[][] matrix = new int[10][10];
    private static int checkedCell = 0;
    private static String[] role = new String[4];
    private static int[] row = new int[10];
    private static int[] column = new int[10];
    private static int xPos, yPos;
    private static int row_flag,  col_flag;
    private int clientNum;
    private int workNum; // 실제 맡은 역할임. 0: row, 1: col, 2: cal, 3: cal

    private static boolean rowReady = false, columnReady = false;

    public ClientHandler(Socket socket, ServerSocket serverSocket, int targetMatriexIndex) {
        this.socket = socket;
        this.serverSocket = serverSocket;
        this.targetMatriexIndex=targetMatriexIndex;
        clientNum = ClientList.getClients(socket);
        if(targetMatriexIndex==1) { // 1이면 역할 변경
            if(clientNum==0) { // 클라 번호에 따라 역할 변경
                workNum=1; // 현재 row col 역할이 calc 역할도 맡으면 에러가 터짐. 그러나 row col 역할이 row col 내 변경은 가능함.
            } else if(clientNum==1) {
                workNum=0;
            } else if(clientNum==2) {
                workNum=3;
            } else if(clientNum==3) {
                workNum=2;
            }
        } else { // 0이면 그대로 AB(row col) CD(calc calc)
            workNum=clientNum;
        }

        // 아래의 if문에 따른 -1 초기화 이해가 안감.!
        System.out.println("clientNum = " + clientNum + " workNum = " + workNum);
        if(clientNum == 0){
            for (int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    matrix[i][j] = -1;
                }
            }
        }
    }

    private static final Object lock = ClientHandler.class;
    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutput = StreamHandler.getOutputStreamMap().get(socket);
            ObjectInputStream objectInput = StreamHandler.getInputStreamMap().get(socket);

            LogHandler logHandler = new LogHandler(clientNum);

            // for(int i = 0; i < 100; i++){} 100라운드 필요
            //objectOutput.writeObject(new Instruction(true,false,false, false)); //새 라운드 시작
            TimeHandler.addTime(ClientList.getClients(socket));
            logHandler.clientLog("[Client" + clientNum + "] New Round 수행\n");
            LogHandler.serverLog("[Client" + clientNum + "] New Round 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");
            while(checkedCell != 100) {
                synchronized (lock){
                    if(workNum == 0){ // 작업 종류에 따른 분기
                        //차례대로 행 입력
                        objectOutput.writeObject(new Instruction(false,true,true, false));
                        int line;
                        if(row_flag != -1) line = row_flag;
                        else line = (int)(Math.random()*10);
                        objectOutput.writeObject(line);
                        xPos = (int) objectInput.readObject();
                        row = (int[]) objectInput.readObject();
                        TimeHandler.addTime(ClientList.getClients(socket));
                        logHandler.clientLog("[Client" + clientNum + "] Get Matrix Row 수행\n");
                        LogHandler.serverLog("[Client" + clientNum + "] Get Matrix Row 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");
                        rowReady = true;
                    }
                }

                synchronized (lock){
                    if(workNum == 1){
                        //열 입력
                        objectOutput.writeObject(new Instruction(false, true, false, false));
                        int line;
                        if(col_flag != -1) line = col_flag;
                        else line = (int)(Math.random()*10);
                        objectOutput.writeObject(line);
                        yPos = (int) objectInput.readObject();
                        column = (int[]) objectInput.readObject();
                        TimeHandler.addTime(ClientList.getClients(socket));
                        logHandler.clientLog("[Client" + clientNum + "] Get Matrix Column 수행\n");
                        LogHandler.serverLog("[Client" + clientNum + "] Get Matrix Column 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");
                        columnReady = true;
                    }
                }

                synchronized (lock){
                    if(workNum == 2){
                        if(rowReady && columnReady){//값을 입력하지 않은 배열이라면 client에서 calc 호출
                            System.out.println("cNum2 수행");
                            objectOutput.writeObject(new Instruction(false,false,false, true));
                            objectOutput.writeObject(row);
                            objectOutput.writeObject(column);
                            TimeHandler.addTime(ClientList.getClients(socket));
                            logHandler.clientLog("[Client" + clientNum + "] Calculation 수행\n");
                            LogHandler.serverLog("[Client" + clientNum + "] Calculation 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");


                            if(matrix[xPos][yPos] == -1){
                                if (xPos == row_flag && yPos == col_flag){
                                    row_flag = -1;
                                    col_flag = -1;
                                }
                                System.out.println("행렬에 값 저장");
                                int answer = (int) objectInput.readObject();
                                matrix[xPos][yPos] = answer;

                                checkedCell++;
                                System.out.println("checkedCell = " + checkedCell);
                                for (int i = 0; i < 10; i++){
                                    for(int j = 0; j < 10; j++){
                                        System.out.print("[" + matrix[i][j] + "] ");
                                    }
                                    System.out.println();
                                }
                            } else{
                                for(;xPos<10;xPos++){
                                    for(;yPos<10;yPos++){
                                        if (matrix[xPos][yPos] == -1){
                                            row_flag = xPos;
                                            col_flag = yPos;
                                        }
                                    }
                                    yPos = 0;
                                }
                                if (row_flag == -1){
                                    for(int i=0;i<10;i++){
                                        for(int j=0;j<10;j++) {
                                            if (matrix[i][j] == -1) {
                                                row_flag = i;
                                                col_flag = j;
                                            }
                                        }
                                    }
                                }
                            }
                            rowReady = false;
                            columnReady = false;//행렬 연산에서 사용할 Row or Column이 준비되지 않았을 때 calc 명령이 시행되는 경우 방지

                        }

                    }
                }

                synchronized (lock){
                    if(workNum == 3){
                        if(rowReady && columnReady){//값을 입력하지 않은 배열이라면 client에서 calc 호출
                            System.out.println("cNum3 수행");
                            objectOutput.writeObject(new Instruction(false,false,false, true));
                            objectOutput.writeObject(row);
                            objectOutput.writeObject(column);
                            TimeHandler.addTime(ClientList.getClients(socket));
                            logHandler.clientLog("[Client" + clientNum + "] Calculation 수행\n");
                            LogHandler.serverLog("[Client" + clientNum + "] Calculation 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");


                            if(matrix[xPos][yPos] == -1){
                                if (xPos == row_flag && yPos == col_flag){
                                    row_flag = -1;
                                    col_flag = -1;
                                }
                                System.out.println("행렬에 값 저장");
                                int answer = (int) objectInput.readObject();
                                matrix[xPos][yPos] = answer;

                                checkedCell++;
                                System.out.println("checkedCell = " + checkedCell);
                                for (int i = 0; i < 10; i++){
                                    for(int j = 0; j < 10; j++){
                                        System.out.print("[" + matrix[i][j] + "] ");
                                    }
                                    System.out.println();
                                }
                            } else{
                                for(;xPos<10;xPos++){
                                    for(;yPos<10;yPos++){
                                        if (matrix[xPos][yPos] == -1){
                                            row_flag = xPos;
                                            col_flag = yPos;
                                        }
                                    }
                                    yPos = 0;
                                }
                                if (row_flag == -1){
                                    for(int i=0;i<10;i++){
                                        for(int j=0;j<10;j++) {
                                            if (matrix[i][j] == -1) {
                                                row_flag = i;
                                                col_flag = j;
                                            }
                                        }
                                    }
                                }
                            }


                            rowReady = false;
                            columnReady = false;//행렬 연산에서 사용할 Row or Column이 준비되지 않았을 때 calc 명령이 시행되는 경우 방지
                        }
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
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

    // 10x10 배열의 레퍼런스를 넘겨주면 행렬 초기화 해줌. (라운드 종료시 새로운 클라이언트 행렬을 생성하는 기능..?)
    public void initMatrix(int[][] targetMatrix) {
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++) {
                targetMatrix[i][j] = (int) (Math.random() * 101);
            }
        }
    }

    public void setRole(int num) {
        if(num==0) {
            role[0] = "row_mat";
            role[1] = "col_mat";
            role[2] = "calc1";
            role[3] = "calc2";
        } else if(num==1) {
            role[0] = "row_mat";
            role[1] = "calc1";
            role[2] = "col_mat";
            role[3] = "calc2";
        } else if(num==2) {
            role[0] = "row_mat";
            role[1] = "calc1";
            role[2] = "calc2";
            role[3] = "col_mat";
        } else if(num==3) {
            role[0] = "calc1";
            role[1] = "row_mat";
            role[2] = "col_mat";
            role[3] = "calc2";
        } else if(num==4) {
            role[0] = "calc1";
            role[1] = "row_mat";
            role[2] = "calc2";
            role[3] = "col_mat";
        } else if(num==5) {
            role[0] = "calc1";
            role[1] = "calc2";
            role[2] = "row_mat";
            role[3] = "col_mat";
        }
    }
}
