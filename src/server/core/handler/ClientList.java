package server.core.handler;

import java.net.Socket;

public class ClientList {

    // i번째 수행할 쌍의 번호 (AB:0 AC:1, ...)
    private static int[] orderSequence = new int[6];

    //현재 실행중인(실행해야할) orderSequence의 위치
    private static int curOrder = 0;
    private static Socket[] clients = new Socket[4];
    private static String[] Role = new String[4];
    // row_mat, col_mat / calc1, calc2
    private static int clientSize = 0;

    public static void addClient(Socket socket) {
        clients[clientSize++] = socket;
    }

    // 0-5를 셔플하여 수행할 순서쌍의 번호를 설정합니다.
    public static void setSequence() {
        boolean[] isEmptyNumber = {true, true, true, true, true, true};

        for(int i=0; i<6; i++) {
            while(true) {
                int num = (int) (Math.random() * 6);
                if (isEmptyNumber[num]) {
                    orderSequence[i] = num;
                    isEmptyNumber[num] = false;
                    break;
                }
            }
        }
    }

    // 순서쌍의 번호로 해당 순서쌍의 Socket를 획득합니다.
    public static Socket[] getSocketPair(int num) {
        Socket[] sockets = new Socket[2];

        // 0  1  2  3  4  5
        // AB AC AD BC BD CD
        if(num==0) {
            sockets[0] = clients[0];
            sockets[1] = clients[1];
        } else if (num==1) {
            sockets[0] = clients[0];
            sockets[1] = clients[2];
        } else if(num==2) {
            sockets[0] = clients[0];
            sockets[1] = clients[3];
        } else if(num==3) {
            sockets[0] = clients[1];
            sockets[1] = clients[2];
        } else if(num==4) {
            sockets[0] = clients[1];
            sockets[1] = clients[3];
        } else if(num==5) {
            sockets[0] = clients[2];
            sockets[1] = clients[3];
        }
        return sockets;
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

    public static Socket[] getClients() {
        return clients;
    }

    public static int getClientSize() {
        return clientSize;
    }
}
