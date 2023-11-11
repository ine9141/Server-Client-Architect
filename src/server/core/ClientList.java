package server.core;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientList {
    private static Map<Socket, Integer> clients = new HashMap<>();
    // row_mat, col_mat / calc1, calc2
    private static int clientSize = 0;

    // i번째 수행할 쌍의 번호 (AB:0 AC:1, ...)
    private static int[] orderSequence = new int[6];

    //현재 실행중인(실행해야할) orderSequence의 위치
    private static int curOrder = 0;

    public static void addClient(Socket socket) {
        clients.put(socket, getClientSize());
        clientSize++;
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

//    // 역할 랜덤 배정
//    public static void setRole() {
//        String[] roleList = {"row_mat", "col_mat", "calc1", "calc2"};
//        boolean[] isEmptyRole = {true, true, true, true};
//
//        for(int i=0; i<4; i++) {
//            while(true) {
//                int num = (int) (Math.random() * 4);
//                if (isEmptyRole[num]) {
//                    role[i] = roleList[num];
//                    isEmptyRole[num] = false;
//                    break;
//                }
//            }
//
//        }
//    }

    // 역할 배정 (현재 순서쌍 번호를 주면 역할을 설정해줌)
//    public static void setRole(int num) {//refactor to ClientHandler
//        if(num==0) {
//            role[0] = "row_mat";
//            role[1] = "col_mat";
//            role[2] = "calc1";
//            role[3] = "calc2";
//        } else if(num==1) {
//            role[0] = "row_mat";
//            role[1] = "calc1";
//            role[2] = "col_mat";
//            role[3] = "calc2";
//        } else if(num==2) {
//            role[0] = "row_mat";
//            role[1] = "calc1";
//            role[2] = "calc2";
//            role[3] = "col_mat";
//        } else if(num==3) {
//            role[0] = "calc1";
//            role[1] = "row_mat";
//            role[2] = "col_mat";
//            role[3] = "calc2";
//        } else if(num==4) {
//            role[0] = "calc1";
//            role[1] = "row_mat";
//            role[2] = "calc2";
//            role[3] = "col_mat";
//        } else if(num==5) {
//            role[0] = "calc1";
//            role[1] = "calc2";
//            role[2] = "row_mat";
//            role[3] = "col_mat";
//        }
//    }

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
