package main;

import game.DoubleBonus710;
import game.GameMode;


public class Main {
    public static void main(String[] args){

        if (args[0].equals("-d")) {
            GameMode doubleBonus710 = new DoubleBonus710(Integer.parseInt(args[1]), args[2], args[3]);
            doubleBonus710.run();
        } else if (args[0].equals("-s")){
            GameMode doubleBonus710 = new DoubleBonus710(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            doubleBonus710.run();
        }
    }
}
