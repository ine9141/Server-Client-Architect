package server.core.handler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


/*
 클라이언트 접속시 StreamHandler에 해당 소켓의 ObjectOutputStream, ObjectInputStream 등록 방법
 Map<Socket, ObjectOutputStream> outputStreamMap = StreamHandler.getOutputStreamMap();
 Map<Socket, ObjectInputStream> inputStreamMap = StreamHandler.getInputStreamMap();
 outputStreamMap.put(socket, new ObjectOutputStream(socket.getOutputStream()));
 inputStreamMap.put(socket, new ObjectInputStream(socket.getInputStream()));
*/
public class StreamHandler {
    private static Map<Socket, ObjectOutputStream> outputStreamMap = new HashMap<>();
    private static Map<Socket, ObjectInputStream> inputStreamMap = new HashMap<>();


    public static Map<Socket, ObjectOutputStream> getOutputStreamMap() {
        return outputStreamMap;
    }

    public static Map<Socket, ObjectInputStream> getInputStreamMap() {
        return inputStreamMap;
    }
}
