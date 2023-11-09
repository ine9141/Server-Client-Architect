package server.core.handler;

import java.net.Socket;

public class Clients {
    private static Socket[] clients = new Socket[4];
    private static String[] Role = new String[4];
    // row_mat, col_mat / calc1, calc2
    private static int clientSize = 0;

    public void addClient(Socket socket) {
        clients[clientSize++] = socket;
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
