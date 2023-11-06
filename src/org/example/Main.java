package org.example;


import org.example.handler.ProgressHandler;
import org.example.handler.RuleHandler;
import org.example.rule.Client;

public class Main {
    public static void main(String[] args) {
        Client[] clients = new Client[4];
        for(int i = 0; i < 4; i++)clients[i] = new Client();
        RuleHandler ruleHandler = new RuleHandler();
        ruleHandler.setRule(clients);

        ProgressHandler progressHandler = new ProgressHandler(clients);
        progressHandler.run();
    }
}