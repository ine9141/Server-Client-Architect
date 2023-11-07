package server.core.handler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogHandler {
    public static void serverLog(String message){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("server.log"))) {
            bw.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Client에 toString override로 ClientN.log 설정
    public static void clientLog(String message, Class client){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(client.toString() + ".log"))) {
            bw.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
