package org.example.role;

public class Provider {
    int[][][] matrix;

    public Provider(){
        matrix = new int[6][10][10];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    matrix[i][j][k] = (int) (Math.random() * 101);
                }
            }
        }
    }

    public int[][][] getMatrix() {
        return matrix;
    }
}
