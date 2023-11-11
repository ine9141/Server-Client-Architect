package server.core.handler;

import server.core.ClientList;

import java.io.IOException;
import java.net.ServerSocket;

public class MatrixHandler implements Runnable {

    private ServerSocket serverSocket;
    private static int[][][] maxtrix = new int[6][10][10];
    private int targetMatrixIndex; // 몇번째 결과 행렬인가?, 0~5를 가짐. 0이면 AB가 Mat CD가 calc임을 알 수 있음.


    public MatrixHandler(ServerSocket serverSocket, int targetMatrixIndex) {
        this.serverSocket = serverSocket;
        this.targetMatrixIndex=targetMatrixIndex;
    }

    // 필요할지 모르겠지만 모든 행렬에 계산 값이 들어갔는지 boolean으로 체크 (계산값이0이면 계산결과인지 아직 계산 전인지 모르기때문)
    private static boolean[][][] visit = new boolean[6][10][10];

    // 행렬 생성
    public static void createMatrix() {
        maxtrix = new int[6][10][10];
    }

    // visit 초기화
    public static void createVisit() {
        visit = new boolean[6][10][10];
    }

    // 현재 모든 행렬 및 시간 찍기
    public static String printMatrix(int round, int time) {

        // [라운드 : 1, 소요시간 : 1]
        // [1번째 행렬]
        // 0 0 0
        // 0 0 0
        // 0 0 0
        // [2번째 행렬]
        // 1 1 1
        // 1 1 1
        // 1 1 1

        String string = "[라운드 : " + round + ", 소요시간 : " + time + "]\n";
        for(int i=0; i<6; i++) {
            string = string.concat("[" + (i+1) + "]번째 행렬\n");
            for(int j=0; j<10; j++) {
                for(int k=0; k<10; k++) {
                    string =string.concat(String.valueOf(maxtrix[i][j][k])+" ");
                }
                string =string.concat("\n");
            }
        }
        string = string.concat("\n");
        return string;
    }

    public static void saveResult(int line1, int line2, int result) {
        // [현재 실행중인 순서쌍의 번호][행][열]
        int[] orderSequence = ClientList.getOrderSequence();
        int curOrder = ClientList.getCurOrder();
        maxtrix[orderSequence[curOrder]][line1][line2] = result;
    }

    public static void saveResult(int pairNumber, int line1, int line2, int result) {
        // [순서쌍의 번호][행][열]
        int[] orderSequence = ClientList.getOrderSequence();
        maxtrix[pairNumber][line1][line2] = result;
    }

    public static void printIndexTargetMatrix(int index)
    {
        String str = (index) + "번째 매트릭스\n";
        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                str = str.concat(maxtrix[index][i][j] + " ");
            }
            str = str.concat("\n");
        }
        str = str.concat("\n");
        System.out.println(str);
    }
    public static int[][][] getMaxtrix() {
        return maxtrix;
    }

    @Override
    public void run() {

        for (int i = 0; i < 4; i++) {
            ClientHandler clientHandler = new ClientHandler(ClientList.getIntegerToSocket().get(i), serverSocket, targetMatrixIndex);
            Thread thread = new Thread(clientHandler);
            thread.start();
        }
    }
}
