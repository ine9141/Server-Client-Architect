package org.example;


import org.example.handler.ProgressHandler;
import org.example.handler.RuleHandler;
import org.example.role.Role;

public class Main {
    public static void main(String[] args) {
        Role[] clients = new Role[4];
        for(int i = 0; i < 4; i++)clients[i] = new Role();
        RuleHandler ruleHandler = new RuleHandler();
        ruleHandler.setRule(clients);

        ProgressHandler progressHandler = new ProgressHandler(clients);
        progressHandler.run();
    }
}