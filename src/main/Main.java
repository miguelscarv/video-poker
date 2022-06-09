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

        GameMode doubleBonus710 = new DoubleBonus710(10000, "/Users/miguelcarvalho/Desktop/card-file.txt",  "/Users/miguelcarvalho/Desktop/cmd-file.txt");

        doubleBonus710.run();

    }
}
