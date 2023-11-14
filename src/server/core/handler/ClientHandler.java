package server.core.handler;

import java.io.*;
import java.net.Socket;
import java.util.*;

import static server.core.handler.ServerLogHandler.*;
import static server.core.handler.TimeHandler.*;

public class ClientHandler extends Thread{ //소켓 접속 때 마다 하나 생김

    private MatrixHandler matrixHandler;
    private HashMap<Integer,Socket> clients;
    private HashMap<Socket,ObjectOutputStream> outputStreams;
    private HashMap<Socket,ObjectInputStream> inputStreams;
    private int cNum;
    private static boolean firstFinish = true;


    public static String[][] combSet = {{"row","col","calc","calc"}, {"col","row","calc","calc"}, {"calc","col","row","calc"},
            {"calc","row","col","calc"}, {"calc","calc","row","col"}, {"calc","calc","col","row"}};
    private static int checkedCell = 0;
    private static int round = 0;


    public ClientHandler(MatrixHandler matrixHandler, int cNum){
        this.matrixHandler = matrixHandler;
        this.cNum = cNum;

        SetupHandler setupHandler = SetupHandler.getInstance();
        System.out.println("ClientHandler - SetupHandler instance: " + setupHandler);
        clients = setupHandler.getClients();
        outputStreams = setupHandler.getOutputStreams();
        inputStreams = setupHandler.getInputStreams();
    }

    @Override
    public void run() {
        int combNum = 0;
        boolean rowReady;
        boolean colReady;
        try {
            outputStreams.get(clients.get(cNum)).writeObject(cNum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (round < 100){
            String[] comb = combSet[combNum];
            Row rowIn = null;
            Column columnIn = null;
            rowReady = false;
            colReady = false;
            int a = -1,b = -1;
            for (int i = 0; i < 4; i++){
                if(comb[i].equals("row")){
                    try {
                        addTime();
                        rowIn = getRow(clients.get(i));
                        serverLog(i + "번 클라이언트에 Get Row 명령\n");
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    a = i;
                    rowReady = true;
                }
                if(comb[i].equals("col")){
                    try {
                        addTime();
                        columnIn = getColumn(clients.get(i));
                        serverLog(i + "번 클라이언트에 Get Column 명령\n");
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    b = i;
                    colReady = true;
                }
            }
            List<Integer> result = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                if (i != a && i != b) {
                    result.add(i);
                }
            }
            int res = result.get((int) (Math.random() * 2));
            if(colReady && rowReady){
                try {
                    addTime();
                    doCalc(clients.get(res), combNum, rowIn, columnIn);
                    serverLog(res + "번 클라이언트에 Calculation 명령\n");
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            if(combNum == 5){
                combNum = 0;
            }else{
                combNum++;
            }

            if(checkedCell == 600){
                checkedCell = 0;
                serverLog(round + " 라운드 수행 시간 : " + getRoundTime() + "\n");
                addRound();
                try {
                    MatrixHandler.printMatrix(round,0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 4; i++){
                    try {
                        outputStreams.get(clients.get(i)).writeObject(0);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                round++;
            }
        }
        if (firstFinish) {
            TimeHandler.printRound();
            try {
                MatrixHandler.printMatrixAtLog();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            serverLog("총 소요시간 : " + getTotalTime());
            firstFinish = false;
        }
    }

    private void doCalc(Socket socket, int combNum, Row rowIn, Column columnIn) throws IOException, ClassNotFoundException {
        synchronized (outputStreams.get(socket)) {
            synchronized (inputStreams.get(socket)){
                if(MatrixHandler.getMatrix(round,combNum,rowIn.xPos,columnIn.yPos) != -1){
                    return;
                }
                ObjectOutputStream objectOutput = outputStreams.get(socket);
                ObjectInputStream objectInput = inputStreams.get(socket);
                objectOutput.writeObject(3);
                objectOutput.writeObject(rowIn.row);
                objectOutput.writeObject(columnIn.column);
                int answer = (int) objectInput.readObject();
                matrixHandler.setMatrix(round, combNum, rowIn.xPos, columnIn.yPos, answer);
            }
        }
    }

    public static void addCheckedCell(){
        checkedCell++;
    }

    private Column getColumn(Socket socket) throws IOException, ClassNotFoundException {
        synchronized (outputStreams.get(socket)) {
            synchronized (inputStreams.get(socket)) {
                ObjectOutputStream objectOutput = outputStreams.get(socket);
                ObjectInputStream objectInput = inputStreams.get(socket);
                int line_col = (int) (Math.random() * 10);
                objectOutput.writeObject(2);
                objectOutput.writeObject(line_col);
                return new Column((int) objectInput.readObject(), (int[]) objectInput.readObject());
            }
        }
    }

    private Row getRow(Socket socket) throws IOException, ClassNotFoundException {
        synchronized (outputStreams.get(socket)) {
            synchronized (inputStreams.get(socket)) {
                ObjectOutputStream objectOutput = outputStreams.get(socket);
                ObjectInputStream objectInput = inputStreams.get(socket);
                int line_row = (int) (Math.random() * 10);
                objectOutput.writeObject(1);
                objectOutput.writeObject(line_row);
                return new Row((int) objectInput.readObject(), (int[]) objectInput.readObject());
            }
        }
    }

    class Row{
        int xPos;
        int[] row;

        public Row(int xPos, int[] row) {
            this.xPos = xPos;
            this.row = row;
        }
    }
    class Column{
        int yPos;
        int[] column;

        public Column(int yPos, int[] column) {
            this.yPos = yPos;
            this.column = column;
        }
    }
}
