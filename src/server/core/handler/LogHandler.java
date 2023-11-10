package server.core.handler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogHandler {
    private int clientNumber;
    BufferedWriter bw;
    public LogHandler(int clientNumber) throws IOException {
        this.clientNumber = clientNumber;
        bw = new BufferedWriter(new FileWriter("Client" + clientNumber + ".log",false));
    }

    public void clientLog(String message) throws IOException {
        bw.write(message);
    }

    public static void serverLog(String message){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("server.log", true))) {
            bw.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
