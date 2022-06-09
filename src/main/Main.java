package main;

import command.CommandType;
import command.FullCommand;
import game.DoubleBonus710;
import game.GameMode;
import game.match.Reader;
import player.Player;
import player.cards.Deck;
import game.match.*;

import java.util.List;

public class Main {
    public static void main(String[] args){

        if (args[0].equals("-d")) {
            GameMode doubleBonus710 = new DoubleBonus710(Integer.parseInt(args[1]), args[2], args[3]);
            doubleBonus710.run();
        } else if (args[0].equals("-s")){
            System.out.println("Not yet implemented...");
        }
    }
}
