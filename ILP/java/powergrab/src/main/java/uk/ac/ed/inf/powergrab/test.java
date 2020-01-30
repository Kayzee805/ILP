package uk.ac.ed.inf.powergrab;

import java.util.Random;

public class test {
    public static void main(String[] args) {
        Random rand = new Random(5678);
        for(int i = 0;i<30;i++) {
            System.out.println(i+" "+rand.nextInt(16));
        }
    }

    
}
