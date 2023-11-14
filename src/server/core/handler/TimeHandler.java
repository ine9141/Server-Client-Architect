package server.core.handler;

public class TimeHandler {
    private static int round = 0;
    private static int roundTime = 0;
    private static int[] roundTimeArr = new int[100];
    private static int totalTime = 0;

    public static void addTime(){
        roundTime++;
        totalTime++;
    }

    public static void addRound(){
        roundTimeArr[round] = roundTime;
        round++;
        roundTime = 0;
    }

    public static void printRound(){
    for (int i = 0; i < 100; i++){
        ServerLogHandler.serverLog(i + " round time : [" + roundTimeArr[i] + "] \n");
    }
    ServerLogHandler.serverLog("\n");
    }
    public static int getRound() {
        return round;
    }

    public static int getRoundTime() {
        return roundTime;
    }

    public static int[] getRoundTimeArr() {
        return roundTimeArr;
    }

    public static int getTotalTime() {
        return totalTime;
    }
}
