package org.example;

import org.example.handler.ProgressHandler;
import org.example.handler.RoleHandler;
import org.example.role.Role;

public class Test {
    public static void main(String[] args) {
        Role[] users = new Role[4];
        for(int i = 0; i < 4; i++){
            users[i] = new Role();
        }

        RoleHandler.setRole(users);
        ProgressHandler progressHandler = new ProgressHandler(users);
        progressHandler.run();
    }
}
