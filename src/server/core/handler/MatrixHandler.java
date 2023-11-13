package server.core.handler;

import java.io.IOException;

import static server.core.handler.ServerLogHandler.*;

public class MatrixHandler {
    private static int[][][][] matrix = new int[100][6][10][10];

    public MatrixHandler(){
        for (int i = 0; i < 100; i++){
            for(int j = 0; j < 6; j++){
                for (int k = 0; k < 10; k++){
                    for(int l = 0; l < 10; l++){
                        matrix[i][j][k][l] = -1;
                    }
                }
            }
        }
    }
    public static void printMatrix(int round, int time) throws IOException {
        int[][][] temp = matrix[round];
        for (int i = 0; i < 6; i++){
            System.out.print("Round : " + (round+1) + "  Comb : " + i);
            serverLog("Round : " + (round+1) + "  Comb : " + i);
            for (int j = 0; j < 10; j++){
                System.out.println();
                serverLog("\n");
                for (int k = 0; k < 10; k++){
                    serverLog("[" + temp[i][j][k]+"] ");
                    System.out.print("[" + temp[i][j][k]+"] ");
                }
            }
            serverLog("\n");
            System.out.println();
        }
    }

    public static void printMatrixAtLog() throws IOException {
        for (int l = 0; l < 100; l++){
            for (int i = 0; i < 6; i++){
                serverLog("Round : " + (l+1) + "  Comb : " + i);
                for (int j = 0; j < 10; j++){
                    serverLog("\n");
                    for (int k = 0; k < 10; k++){
                        serverLog("[" + matrix[l][i][j][k]+"] ");
                    }
                }
                serverLog("\n");
            }
        }
    }

    public void setMatrix(int round, int comb, int row, int col, int answer){
        int[][][] tempMatrix = matrix[round];
        int[][] tempTwoMatrix = tempMatrix[comb];
        if (tempTwoMatrix[row][col] == -1){
            tempTwoMatrix[row][col] = answer;
            ClientHandler.addCheckedCell();
        }
        else{
            return;
        }
    }

    public static int getMatrix(int round, int comb, int row, int col){
        return matrix[round][comb][row][col];
    }
}
