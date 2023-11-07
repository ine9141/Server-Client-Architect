package server.core.handler;

public class TimeHandler {
    private static int[] clientTime;//클라이언트별 시간
    private static int serverTime;
    private static int round;
    private static int totalTime;

    public TimeHandler(){
        clientTime = new int[4]; round = 0; totalTime = 0; serverTime = 0;
        for (int i = 0; i < 4; i++){
            clientTime[i] = 0;
        }
    }

    //Server 동작 때 마다 이 메서드 call
    public static void addTime(){
        serverTime++;
        totalTime++;
    }
    //Client 동작 때 마다 이 메서드 call
    public static void addTime(String clientInfo){
        clientTime[Integer.parseInt(clientInfo)]++;
        totalTime++;
    }

    public static void addRound(){
        for (int i = 0; i < 4; i++){
            totalTime += clientTime[i];
            clientTime[i] = 0;
        }
        serverTime = 0;
        round++;
    }

    public static int getRound(){
        return round;
    }

    public static int getTotalTime(){
        return totalTime;
    }

    public static int getClientTime(String clientInfo){
        return clientTime[Integer.parseInt(clientInfo)];
    }

    public static int getServerTime(){
        return serverTime;
    }

    @Override
    public String toString(){
        return "Round : " + round + ", Total : " + totalTime;
    }
}
