package org.example.rule;

public class Client {
    public static Calculator calculator1;
    public static Calculator calculator2;
    public static Provider provider;
    public static Receiver receiver;
    int rule;
    public void isCalculator1(){
        rule = 0;
        calculator1 = new Calculator();
        calculator2 = null;
        provider = null;
        receiver = null;
    }
    public void isCalculator2(){
        rule = 1;
        calculator1 = null;
        calculator2 = new Calculator();
        provider = null;
        receiver = null;
    }
    public void isReceiver(){
        rule = 2;
        calculator1 = null;
        calculator2 = null;
        provider = null;
        receiver = new Receiver();
    }

    public void isProvider(){
        rule = 3;
        calculator1 = null;
        calculator2 = null;
        provider = new Provider();
        receiver = null;
    }
    public int getRule(){
        return rule;
    }

    public Calculator getCalculator1() {
        return calculator1;
    }

    public Calculator getCalculator2() {
        return calculator2;
    }

    public Provider getProvider() {
        return provider;
    }

    public Receiver getReceiver() {
        return receiver;
    }
}
