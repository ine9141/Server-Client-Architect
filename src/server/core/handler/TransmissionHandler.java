package server.core.handler;

public class TransmissionHandler {
    public TransmissionHandler(){
        row = new int[6][10];
        col = new int[6][10];
        xPos = new int[6];
        yPos = new int[6];
        checkedCell = new int[]{0,0,0,0,0,0};
        row_flag = new int[6];
        col_flag = new int[6];
        rowReady = new boolean[]{false,false,false,false,false,false};
        columnReady = new boolean[]{false,false,false,false,false,false};
    }

    public static int[][] row;
    public static int[][] col;
    public static int[] xPos;
    public static int[] yPos;
    public static int[] checkedCell;
    public static int[] row_flag;
    public static int[] col_flag;
    public static boolean[] rowReady;
    public static boolean[] columnReady;
}
