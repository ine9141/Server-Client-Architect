package cli.java.org.example;

import java.util.Arrays;

public class ArrayConvert {
    private int[] data;
    private int line;
    private int compNum;

    public ArrayConvert(int[] data, int compNum, int line) {
        this.line = line;
        this.data = data;
        this.compNum = compNum;
    }

    public String toString(){
        String intValue = Arrays.toString(data);
        String resultString = intValue.replaceAll("[^0-9\\s]", "");
        return compNum + " " + line + " " + resultString ;
    }

}
