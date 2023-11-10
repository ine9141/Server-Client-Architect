package server.core.handler;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientList {
    private static Map<Socket, Integer> clients = new HashMap<>();
    private static String[] Role = new String[4];
    // row_mat, col_mat / calc1, calc2
    private static int clientSize = 0;

    // i번째 수행할 쌍의 번호 (AB:0 AC:1, ...)
    private static int[] orderSequence = new int[6];

    //현재 실행중인(실행해야할) orderSequence의 위치
    private static int curOrder = 0;

    public static void addClient(Socket socket) {
        clients.put(socket, getClientSize());
    }

    // 역할 랜덤 배정
    public static void setRole() {
        String[] roleList = {"row_mat", "col_mat", "calc1", "calc2"};
        boolean[] isEmptyRole = {true, true, true, true};

        for(int i=0; i<4; i++) {
            while(true) {
                int num = (int) (Math.random() * 4);
                if (isEmptyRole[num]) {
                    Role[i] = roleList[num];
                    isEmptyRole[num] = false;
                    break;
                }
            }

        }
    }

    // 역할 배정 (현재 순서쌍 번호를 주면 역할을 설정해줌)
    public static void setRole2(int num) {
        if(num==0) {
            Role[0] = "row_mat";
            Role[1] = "col_mat";
            Role[2] = "calc1";
            Role[3] = "calc2";
        } else if(num==1) {
            Role[0] = "row_mat";
            Role[1] = "calc1";
            Role[2] = "col_mat";
            Role[3] = "calc2";
        } else if(num==2) {
            Role[0] = "row_mat";
            Role[1] = "calc1";
            Role[2] = "calc2";
            Role[3] = "col_mat";
        } else if(num==3) {
            Role[0] = "calc1";
            Role[1] = "row_mat";
            Role[2] = "col_mat";
            Role[3] = "calc2";
        } else if(num==4) {
            Role[0] = "calc1";
            Role[1] = "row_mat";
            Role[2] = "calc2";
            Role[3] = "col_mat";
        } else if(num==5) {
            Role[0] = "calc1";
            Role[1] = "calc2";
            Role[2] = "row_mat";
            Role[3] = "col_mat";
        }
    }

    public void initCurOrder() {
        curOrder=0;
    }

    public static int getCurOrder() {
        return curOrder;
    }

    public static int[] getOrderSequence() {
        return orderSequence;
    }

    public static int getClients(Socket socket) {
        return clients.get(socket);
    }

    public static int getClientSize() {
        return clientSize;
    }
}
