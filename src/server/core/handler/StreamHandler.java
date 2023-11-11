package server.core.handler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
