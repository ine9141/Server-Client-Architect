package org.example.role;

public class Receiver {
    public int[][][] matrix;
    public int[][][] result;
    public int[][][] externalMatrix;
    public Receiver(){
        matrix = new int[6][10][10];
        result = new int[6][10][10];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    matrix[i][j][k] = (int) (Math.random() * 101);
                    result[i][j][k] = 0;
                }
            }
        }
    }
}
