package server.core.handler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ServerLogHandler {
    public static void initFile() throws IOException {
        FileWriter fw = new FileWriter("Server.log");
        fw.write("");
        fw.close();
    }
    public static void clientLog(int clientNumber, String message){
        String path = "Client" + clientNumber + ".log";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serverLog(String message){
        try (BufferedWriter serverBw = new BufferedWriter(new FileWriter("Server.log", true))) {
            serverBw.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
