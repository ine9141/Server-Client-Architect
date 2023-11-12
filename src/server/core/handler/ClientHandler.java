package server.core.handler;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable {//소켓 접속 때 마다 하나 생김

    private Socket socket;
    private ServerSocket serverSocket;
    private MatrixHandler matrixHandler;
    private static int[][] matrix = new int[10][10];
    private static int checkedCell = 0;
    private static String[] role = new String[4];
    private static int[] row = new int[10];
    private static int[] column = new int[10];
    private static int xPos, yPos;
    private static int row_flag,  col_flag;
    private int clientNum;
    private int round;
    private int c;
    private static boolean rowReady = false, columnReady = false;

    private ObjectOutputStream objectOutput;
    private ObjectInputStream objectInput;

    public ClientHandler(Socket socket, ServerSocket serverSocket, int clientNum, int round, int c, MatrixHandler matrixHandler,ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.socket = socket;
        this.serverSocket = serverSocket;
        this.clientNum = clientNum;
        this.round = round;
        this.c = c;
        this.matrixHandler = matrixHandler;
        this.objectInput = objectInputStream;
        this.objectOutput = objectOutputStream;

        for(int i = 0 ; i < 10; i++){
            for(int j = 0 ; j < 10; j++) matrix[i][j] = -1;
        }
    }

    private static final Object lock = ClientHandler.class;
    @Override
    public void run() {
        try {

            LogHandler logHandler = new LogHandler(clientNum);

            //새 라운드 시작
            objectOutput.writeObject(0);
            TimeHandler.addTime(clientNum);
            logHandler.clientLog("[Client" + clientNum + "] New Round 수행\n");
            LogHandler.serverLog("[Client" + clientNum + "] New Round 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");


            while(checkedCell != 100) {
                //row 전송
                synchronized (lock){
                    if(clientNum == 0){
                        //차례대로 행 입력
                        objectOutput.writeObject(1);
                        int line_row;
                        if(row_flag != -1) line_row = row_flag;
                        else line_row = (int)(Math.random()*10);
                        objectOutput.writeObject(line_row);
                        xPos = (int) objectInput.readObject();
                        row = (int[]) objectInput.readObject();
                        TimeHandler.addTime(clientNum);
                        logHandler.clientLog("[Client" + clientNum + "] Get Matrix Row 수행\n");
                        LogHandler.serverLog("[Client" + clientNum + "] Get Matrix Row 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");
                        rowReady = true;
                    }
                }

                //col 전송
                synchronized (lock){
                    if(clientNum == 1){
                        //열 입력
                        objectOutput.writeObject(2);
                        int line_col;
                        if(col_flag != -1) line_col = col_flag;
                        else line_col = (int)(Math.random()*10);
                        objectOutput.writeObject(line_col);
                        yPos = (int) objectInput.readObject();
                        column = (int[]) objectInput.readObject();
                        TimeHandler.addTime(clientNum);
                        logHandler.clientLog("[Client" + clientNum + "] Get Matrix Column 수행\n");
                        LogHandler.serverLog("[Client" + clientNum + "] Get Matrix Column 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");
                        columnReady = true;
                    }
                }

                //계산
                synchronized (lock){
                    if(clientNum == 2 || clientNum == 3){
                        if(rowReady && columnReady){//값을 입력하지 않은 배열이라면 client에서 calc 호출
                            if (clientNum == 2) System.out.println("cNum2 수행");
                            else System.out.println("cNum3 수행");
                            objectOutput.writeObject(3);
                            objectOutput.writeObject(row);
                            objectOutput.writeObject(column);
                            TimeHandler.addTime(clientNum);
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
                                MatrixHandler.setMatrix(round,c,xPos,yPos,answer);


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

        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
