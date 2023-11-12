package server.core.handler;

public class MatrixHandler {
    public MatrixHandler(){
        matrix = new int[100][6][10][10];
        matLen = new int[100][6];
        matrixFlag = false;
        nextRoundFlag = false;
    }
    private static int[][][][] matrix;
    private static int[][] matLen;

    public static boolean matrixFlag;
    public static boolean nextRoundFlag;

    public static void setMatrix(int round, int comb, int row, int col, int answer){
        matrix[round][comb][row][col] = answer;
    }

    public static void setMatLen(int round, int comb){
        matLen[round][comb]++;
    }

    public static int getMatrix(int round, int comb, int row, int col){
        return matrix[round][comb][row][col];
    }

    public static int getMatLen(int round, int comb){
        return matLen[round][comb];
    }

    public static int findNextComb(int round, int c) {
        boolean i_flag = false;
        int ret = -1;
        for (int i = c; i < 6; i++) {
            if (MatrixHandler.getMatLen(round, i) != 100) {
                ret = i;
                i_flag = true;
            }
        }
        if (!i_flag){
            for (int i = 0; i < 6; i++) {
                if (MatrixHandler.getMatLen(round, i) != 100) {
                    ret = i;
                    break;
                }
            }
        }
        return ret;
    }

    public static void updateNextRoundFlag(int round){
        int sum = 0;
        for(int i = 0; i< 6; i++){
            sum+=getMatLen(round,i);
        }
        if (sum == 600){
            nextRoundFlag = true;
        } else{
            nextRoundFlag = false;
        }
    }
}
