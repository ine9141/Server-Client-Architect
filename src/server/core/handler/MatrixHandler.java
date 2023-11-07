package server.core.handler;

public class MatrixHandler {

    private static int[][][] maxtrix = new int[6][10][10];

    // 행렬 생성
    public static void createMatrix() {
        maxtrix = new int[6][10][10];
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

    public static int[][][] getMaxtrix() {
        return maxtrix;
    }

}
