package server.core.handler;

import server.core.ClientList;
import server.core.Instruction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ClientHandler implements Runnable {//소켓 접속 때 마다 하나 생김

    private Socket socket;
    private ServerSocket serverSocket;

    private static int[][] matrix = new int[10][10];
    private static int checkedCell = 0;
    private static int[] finishedList = {0,0,0,0,0,0,0,0,0,0};

    private static String[] role = new String[4];
    private static int[] row = new int[10];
    private static int[] column = new int[10];
    private static int xPos, yPos;
    private int clientNum;

    private static boolean rowReady = false, columnReady = false;

    public ClientHandler(Socket socket, ServerSocket serverSocket) {
        this.socket = socket;
        this.serverSocket = serverSocket;
        clientNum = ClientList.getClients(socket);
        System.out.println("clientNum = " + clientNum);
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
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
            
            LogHandler logHandler = new LogHandler(clientNum);
            // for(int i = 0; i < 100; i++){} 100라운드 필요
            objectOutput.writeObject(new Instruction(true,false,false, false)); //새 라운드 시작
            TimeHandler.addTime(ClientList.getClients(socket));
            logHandler.clientLog("[Client" + clientNum + "] New Round 수행\n");
            LogHandler.serverLog("[Client" + clientNum + "] New Round 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");
            while(checkedCell != 100) {
                synchronized (lock){
                    if(clientNum == 0){
                        //차례대로 행 입력
                        objectOutput.writeObject(new Instruction(false,true,true, false));
                        objectOutput.writeObject(Arrays.toString(finishedList));
                        xPos = (int) objectInput.readObject();
                        row = (int[]) objectInput.readObject();
                        TimeHandler.addTime(ClientList.getClients(socket));
                        logHandler.clientLog("[Client" + clientNum + "] Get Matrix Row 수행\n");
                        LogHandler.serverLog("[Client" + clientNum + "] Get Matrix Row 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");
                        rowReady = true;
                    }
                }

                synchronized (lock){
                    if(clientNum == 1){
                        //열 입력
                        objectOutput.writeObject(new Instruction(false, true, false, false));
                        yPos = (int) objectInput.readObject();
                        column = (int[]) objectInput.readObject();
                        TimeHandler.addTime(ClientList.getClients(socket));
                        logHandler.clientLog("[Client" + clientNum + "] Get Matrix Column 수행\n");
                        LogHandler.serverLog("[Client" + clientNum + "] Get Matrix Column 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");
                        columnReady = true;
                    }
                }

                synchronized (lock){
                    if(clientNum == 2){
                        if(rowReady && columnReady){//값을 입력하지 않은 배열이라면 client에서 calc 호출
                            System.out.println("cNum2 수행");
                            objectOutput.writeObject(new Instruction(false,false,false, true));
                            objectOutput.writeObject(row);
                            objectOutput.writeObject(column);
                            TimeHandler.addTime(ClientList.getClients(socket));
                            logHandler.clientLog("[Client" + clientNum + "] Calculation 수행\n");
                            LogHandler.serverLog("[Client" + clientNum + "] Calculation 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");


                            if(matrix[xPos][yPos] == -1){
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
                                if(checkedCell < 10){
                                    boolean isFinishedRaw = true;
                                    for (int i = 0; i < 10; i++){
                                        for (int j = 0; j < 10; j++){
                                            if (matrix[j][i] == -1)isFinishedRaw = false;
                                        }
                                        if (isFinishedRaw) finishedList[i] = 1;
                                    }
                                }
                            }
                            rowReady = false;
                            columnReady = false;//행렬 연산에서 사용할 Row or Column이 준비되지 않았을 때 calc 명령이 시행되는 경우 방지
                        }
                    }
                }

                synchronized (lock){
                    if(clientNum == 3){
                        if(rowReady && columnReady){//값을 입력하지 않은 배열이라면 client에서 calc 호출
                            System.out.println("cNum3 수행");
                            objectOutput.writeObject(new Instruction(false,false,false, true));
                            objectOutput.writeObject(row);
                            objectOutput.writeObject(column);
                            TimeHandler.addTime(ClientList.getClients(socket));
                            logHandler.clientLog("[Client" + clientNum + "] Calculation 수행\n");
                            LogHandler.serverLog("[Client" + clientNum + "] Calculation 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");


                            if(matrix[xPos][yPos] == -1){
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
                            }
                            rowReady = false;
                            columnReady = false;//행렬 연산에서 사용할 Row or Column이 준비되지 않았을 때 calc 명령이 시행되는 경우 방지
                        }
                    }
                }
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
