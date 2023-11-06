package org.example.handler;

import org.example.rule.Calculator;
import org.example.rule.Client;
import org.example.rule.Provider;
import org.example.rule.Receiver;

public class ProgressHandler {
    Client[] client;
    Calculator calculator1;
    Calculator calculator2;
    Receiver receiver;
    Provider provider;
    public ProgressHandler(Client[] client){
        this.client = client;
        for(int i = 0; i < 4; i++){
            //calc1 = 0 calc2 = 1 receiver = 2 provider = 3
            if(client[i].getRule() == 0){
                calculator1 = client[i].getCalculator1();
            }
            else if(client[i].getRule() == 1){
                calculator2 = client[i].getCalculator2();
            }
            else if(client[i].getRule() == 2){
                receiver = client[i].getReceiver();
            }
            else if(client[i].getRule() == 3){
                provider = client[i].getProvider();
            }
        }
        receiver.externalMatrix = provider.getMatrix();
    }

    public void run(){
        boolean flip = true;
        for(int c = 0; c < 6; c++){
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    for(int k = 0; k < 10; k++){
                        if(flip){
                            flip = false;
                            receiver.result[c][i][j] += calculator1.calculate(
                                    receiver.matrix[c][i][k], receiver.externalMatrix[c][k][j]);
                        }else{
                            flip = true;
                            receiver.result[c][j][k] += calculator2.calculate(
                                    receiver.matrix[c][i][k], receiver.externalMatrix[c][k][j]);
                        }
                    }
                }
            }
        }

        printMatrix();
    }

    public void printMatrix(){
        for (int i = 0; i < 6; i++) {
            System.out.println(i + "번 행렬 출력합니다.");
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    System.out.print(receiver.result[i][row][col] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
