package server.core.handler;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientHandler implements Runnable {//소켓 접속 때 마다 하나 생김
    private final int clientNum;
    private final int round;
    private final int c;
    private final ObjectOutputStream objectOutput;
    private final ObjectInputStream objectInput;

    public ClientHandler(int clientNum, int round, int c, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.clientNum = clientNum;
        this.round = round;
        this.c = c;
        this.objectInput = objectInputStream;
        this.objectOutput = objectOutputStream;

        for(int i = 0; i<10;i++){
            for(int j=0;j<10;j++) MatrixHandler.setMatrix(round,c,i,j,-1);
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

            while(true) {
                //row 전송
                synchronized (lock){
                    if(clientNum == 0 && !TransmissionHandler.rowReady[c]){
                        //차례대로 행 입력
                        int line_row;
                        if(TransmissionHandler.row_flag[c] != -1) line_row = TransmissionHandler.row_flag[c];
                        else line_row = (int)(Math.random()*10);
                        objectOutput.writeObject(1);
                        objectOutput.writeObject(line_row);
                        TransmissionHandler.xPos[c] = (int) objectInput.readObject();
                        TransmissionHandler.row[c] = (int[]) objectInput.readObject();
                        TimeHandler.addTime(clientNum);
                        logHandler.clientLog("[Client" + clientNum + "] Get Matrix Row 수행\n");
                        LogHandler.serverLog("[Client" + clientNum + "] Get Matrix Row 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");
                        TransmissionHandler.rowReady[c] = true;
                    }
                }

                //col 전송
                synchronized (lock){
                    if(clientNum == 1 && !TransmissionHandler.columnReady[c]){
                        //열 입력
                        int line_col;
                        if(TransmissionHandler.col_flag[c] != -1) line_col = TransmissionHandler.col_flag[c];
                        else line_col = (int)(Math.random()*10);
                        objectOutput.writeObject(2);
                        objectOutput.writeObject(line_col);
                        TransmissionHandler.yPos[c] = (int) objectInput.readObject();
                        TransmissionHandler.col[c] = (int[]) objectInput.readObject();
                        TimeHandler.addTime(clientNum);
                        logHandler.clientLog("[Client" + clientNum + "] Get Matrix Column 수행\n");
                        LogHandler.serverLog("[Client" + clientNum + "] Get Matrix Column 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");
                        TransmissionHandler.columnReady[c] = true;
                    }
                }

                //계산
                synchronized (lock){
                    if(clientNum == 2 || clientNum == 3){
                        if(TransmissionHandler.rowReady[c] && TransmissionHandler.columnReady[c]){//값을 입력하지 않은 배열이라면 client에서 calc 호출
                            TransmissionHandler.rowReady[c] = false;
                            TransmissionHandler.columnReady[c] = false;
                            if (clientNum == 2) System.out.println("cNum2 수행");
                            else System.out.println("cNum3 수행");
                            objectOutput.writeObject(3);
                            objectOutput.writeObject(TransmissionHandler.row[c]);
                            objectOutput.writeObject(TransmissionHandler.col[c]);
                            TimeHandler.addTime(clientNum);
                            logHandler.clientLog("[Client" + clientNum + "] Calculation 수행\n");
                            LogHandler.serverLog("[Client" + clientNum + "] Calculation 수행. Server Time : " + TimeHandler.getClientTime(clientNum) + "\n");


                            if(MatrixHandler.getMatrix(round,c, TransmissionHandler.xPos[c], TransmissionHandler.yPos[c]) == -1){
                                if (TransmissionHandler.xPos[c] == TransmissionHandler.row_flag[c] && TransmissionHandler.yPos[c] == TransmissionHandler.col_flag[c]){
                                    TransmissionHandler.row_flag[c] = -1;
                                    TransmissionHandler.col_flag[c] = -1;
                                }
                                System.out.println("행렬에 값 저장");
                                int answer = (int) objectInput.readObject();
                                MatrixHandler.setMatrix(round,c, TransmissionHandler.xPos[c], TransmissionHandler.yPos[c],answer);
                                MatrixHandler.setMatLen(round,c);
                                TransmissionHandler.checkedCell[c]++;
                                System.out.println("combNum = "+c+", checkedCell = " + TransmissionHandler.checkedCell[c]);
                                for (int i = 0; i < 10; i++){
                                    for(int j = 0; j < 10; j++){
                                        System.out.print("[" + MatrixHandler.getMatrix(round,c,i,j) + "] ");
                                    }
                                    System.out.println();
                                }
                                MatrixHandler.matrixFlag = true;
                                break;
                            } else{
                                for(; TransmissionHandler.xPos[c]<10; TransmissionHandler.xPos[c]++){
                                    for(; TransmissionHandler.yPos[c]<10; TransmissionHandler.yPos[c]++){
                                        if (MatrixHandler.getMatrix(round,c, TransmissionHandler.xPos[c], TransmissionHandler.yPos[c]) == -1){
                                            TransmissionHandler.row_flag[c] = TransmissionHandler.xPos[c];
                                            TransmissionHandler.col_flag[c] = TransmissionHandler.yPos[c];
                                        }
                                    }
                                    TransmissionHandler.yPos[c] = 0;
                                }
                                if (TransmissionHandler.row_flag[c] == -1){
                                    for(int i=0;i<10;i++){
                                        for(int j=0;j<10;j++) {
                                            if (MatrixHandler.getMatrix(round,c,i,j) == -1) {
                                                TransmissionHandler.row_flag[c] = i;
                                                TransmissionHandler.col_flag[c] = j;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
