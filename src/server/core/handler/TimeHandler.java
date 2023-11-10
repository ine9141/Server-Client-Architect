package server.core.handler;

public class TimeHandler {
    private static int[] clientTime = new int[4];//클라이언트별 시간
    private static int roundTime;
    private static int round;
    private static int totalTime;

    public TimeHandler(){
        clientTime = new int[4]; round = 0; totalTime = 0; roundTime = 0;
        for (int i = 0; i < 4; i++){
            clientTime[i] = 0;
        }
    }

    //Server 동작 때 마다 이 메서드 call
//    public static void addTime(){
//        roundTime++;
//        totalTime++;
//    }
    //Client 동작 때 마다 이 메서드 call
    public static void addTime(int clientNumber){
        clientTime[clientNumber]++;
        totalTime++;
        roundTime++;
    }

    public static void addRound(){
        for (int i = 0; i < 4; i++){
            totalTime += clientTime[i];
            clientTime[i] = 0;
        }
        roundTime = 0;
        round++;
    }

    public static int getRound(){
        return round;
    }

    public static int getTotalTime(){
        return totalTime;
    }

    public static int getClientTime(int clientNumber){
        return clientTime[clientNumber];
    }

    public static int getRoundTime(){
        return roundTime;
    }

    @Override
    public String toString(){
        return "Round : " + round + ", Total : " + totalTime;
    }
}
