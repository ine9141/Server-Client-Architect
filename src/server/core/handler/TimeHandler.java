package server.core.handler;

public class TimeHandler {
    private static int time;
    private static int round;

    public TimeHandler(){
        time = 0; round = 0;
    }
    public static void addTime(){
        time++;
    }

    public static void addRound(){
        round++;
    }

    public static int getRound(){
        return round;
    }

    public static int getTime(){
        return time;
    }

    @Override
    public String toString(){
        return "Round : " + round + ", Time : " + time;
    }
}
