package server.core;

import java.io.Serializable;

public class Instruction implements Serializable {
    private int mode;

    //new round = 0
    //matrix + row = 1
    //matrix + column = 2
    //calculation = 3

    public Instruction(boolean isNewRound, boolean isMatrix, boolean isRow, boolean isCalc) {

        if(isNewRound){
            mode = 0;
        }
        else if (isMatrix){
            if(isRow){
                mode = 1;
            }
            else{
                mode = 2;
            }
        }
        else if(isCalc) mode = 3;
    }

    public String toString(){
        return Integer.toString(mode);
    }
}

