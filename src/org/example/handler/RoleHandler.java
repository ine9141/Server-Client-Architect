package org.example.handler;

import org.example.role.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoleHandler {
    //통신으로 들어온 순서대로 Cleint 배열에 위치시킨 후 역할 분배
    public static void setRole(Role[] clients){
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        for(int i = 0; i < 4; i++){
            int rule = list.get(i);
            if (rule == 1)clients[i].isCalculator1();
            if (rule == 2)clients[i].isCalculator2();
            if (rule == 3)clients[i].isProvider();
            if (rule == 4)clients[i].isReceiver();
        }
    }
}
