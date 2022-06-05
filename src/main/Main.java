package main;

import match.FullCommand;
import match.Reader;
import player.cards.Deck;


import java.util.List;

public class Main {
    public static void main(String[] args){

        Deck deck = new Deck(Reader.readCardFile("/Users/miguelcarvalho/Desktop/card-file.txt"));

        List<FullCommand> fullCommandList = Reader.readCommandFile("/Users/miguelcarvalho/Desktop/cmd-file.txt");

        for (FullCommand command: fullCommandList){
            System.out.println(command);
        }

    }
}
