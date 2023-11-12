package server.core;

public class ArrayConvert {
    private int clientNum;
    private int mode;

    public ArrayConvert(int mode, int clientNum) {
        this.clientNum = clientNum;
        this.mode = mode;
    }

    public String toString(){
        return clientNum + " " + mode;
    }

}
