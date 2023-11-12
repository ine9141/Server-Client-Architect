package server.core.handler;

public class MatrixHandler {
    private static int[][][][] matrix = new int[100][6][10][10];

    public static void setMatrix(int round, int comb, int row, int col, int answer){
        matrix[round][comb][row][col] = answer;
    }

    public static int getMatrix(int round, int comb, int row, int col){
        return matrix[round][comb][row][col];
    }
}
