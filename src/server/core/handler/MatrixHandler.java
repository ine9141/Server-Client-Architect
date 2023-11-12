package server.core.handler;

public class MatrixHandler {
    private static int[][][][] matrix = new int[100][6][10][10];

    public static String printMatrix(int round, int time) {
        String string = "[라운드 : " + round + ", 소요시간 : " + time + "]\n";
        for(int i=0; i<6; i++) {
            string = string.concat("[" + (i+1) + "]번째 행렬\n");
            for(int j=0; j<10; j++) {
                for(int k=0; k<10; k++) {
                    string =string.concat(String.valueOf(matrix[round][i][j][k])+" ");
                }
                string =string.concat("\n");
            }
        }
        string = string.concat("\n");
        return string;
    }

    public void setMatrix(int round, int comb, int row, int col, int answer){
        matrix[round][comb][row][col] = answer;
    }

    public static int getMatrix(int round, int comb, int row, int col){
        return matrix[round][comb][row][col];
    }
}
