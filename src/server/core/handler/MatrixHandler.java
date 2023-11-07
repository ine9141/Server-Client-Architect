package server.core.handler;

public class MatrixHandler {

    private static int[][][] maxtrix;

    // 행렬 생성
    public static void createMatrix() {
        maxtrix = new int[6][10][10];
    }

    public static int[][][] getMaxtrix() {
        return maxtrix;
    }

}
