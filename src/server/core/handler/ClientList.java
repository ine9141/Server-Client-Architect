package server.core.handler;

import java.io.IOException;
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
        // "row_mat", "col_mat", "calc1", "calc2"
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

    // 연결된 클라이언트들 끊기 (종료 로그 찍을때 활용)
    public static void disconnectClients() throws IOException {

        // i번째 클라이언트 종료
        for(int i=0; i<4; i++) {
            clients[i].close();
        }

    }

    public static Socket[] getClients() {
        return clients;
    }

    public static int getClientSize() {
        return clientSize;
    }
}
