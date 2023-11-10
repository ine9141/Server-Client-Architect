package server.core.handler;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientList {
    private static Map<Socket, Integer> clients = new HashMap<>();
    private static String[] Role = new String[4];
    // row_mat, col_mat / calc1, calc2
    private static int clientSize = 0;

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

    public static int getClients(Socket socket) {
        return clients.get(socket);
    }

    public static int getClientSize() {
        return clientSize;
    }
}
