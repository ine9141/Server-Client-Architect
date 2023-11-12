package server.core.handler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ClientHandler extends Thread{ //소켓 접속 때 마다 하나 생김

    private MatrixHandler matrixHandler;
    private HashMap<Integer,Socket> clients;
    private HashMap<Socket,ObjectOutputStream> outputStreams = new HashMap<>();
    private HashMap<Socket,ObjectInputStream> inputStreams = new HashMap<>();

    public static String[][] combSet = {{"row","col","calc","calc"}, {"col","row","calc","calc"}, {"calc","col","row","calc"},
            {"calc","row","col","calc"}, {"calc","calc","row","col"}, {"calc","calc","col","row"}};
    private static int checkedCell = 0;
    private static int round = 0;


    public ClientHandler(MatrixHandler matrixHandler, HashMap<Integer,Socket> clients) throws IOException {
        this.matrixHandler = matrixHandler;
        this.clients = clients;
        for (int i = 0; i < 4; i++){
            outputStreams.put(clients.get(i), new ObjectOutputStream(clients.get(i).getOutputStream()));
            inputStreams.put(clients.get(i), new ObjectInputStream(clients.get(i).getInputStream()));
        }
    }

    @Override
    public void run() {
        int combNum = 0;
        boolean rowReady;
        boolean colReady;
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
                        rowIn = getRow(clients.get(i));
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    a = i;
                    rowReady = true;
                }
                if(comb[i].equals("col")){
                    try {
                        columnIn = getColumn(clients.get(i));
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
                    doCalc(clients.get(res), combNum, rowIn, columnIn);
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
                MatrixHandler.printMatrix(round,0);
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
    }

    private void doCalc(Socket socket, int combNum, Row rowIn, Column columnIn) throws IOException, ClassNotFoundException {
        ObjectOutputStream objectOutput = outputStreams.get(socket);
        ObjectInputStream objectInput = inputStreams.get(socket);
        objectOutput.writeObject(3);
        objectOutput.writeObject(rowIn.row);
        objectOutput.writeObject(columnIn.column);
        int answer = (int) objectInput.readObject();
        matrixHandler.setMatrix(round, combNum, rowIn.xPos, columnIn.yPos, answer);
    }

    public static void addCheckedCell(){
        checkedCell++;
    }

    private Column getColumn(Socket socket) throws IOException, ClassNotFoundException {
        ObjectOutputStream objectOutput = outputStreams.get(socket);
        ObjectInputStream objectInput = inputStreams.get(socket);
        int line_col = (int)(Math.random()*10);
        objectOutput.writeObject(2);
        objectOutput.writeObject(line_col);

        return new Column((int) objectInput.readObject(),(int[]) objectInput.readObject());
    }

    private Row getRow(Socket socket) throws IOException, ClassNotFoundException {
        ObjectOutputStream objectOutput = outputStreams.get(socket);
        ObjectInputStream objectInput = inputStreams.get(socket);
        int line_row = (int)(Math.random()*10);
        objectOutput.writeObject(1);
        objectOutput.writeObject(line_row);

        return new Row((int) objectInput.readObject(),(int[]) objectInput.readObject());
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
