package cli.java.org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLogHandler {
    public static void initFile(int clientNum) throws IOException {
        FileWriter fw = new FileWriter("Client" + clientNum + ".log");
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
}
