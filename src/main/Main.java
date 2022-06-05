package main;

import match.FullCommand;
import match.ReadFile;
import player.cards.Deck;
import states.State;
import states.Timetocheck;
import states.Timetodeal;
import states.Timetodecide;


import java.util.List;

public class Main {
    public static void main(String[] args){

        ReadFile.readCardFile("/Users/miguelcarvalho/Desktop/card-file.txt");
        Deck deck = new Deck(ReadFile.deck);

        ReadFile.readCommandFile("/Users/miguelcarvalho/Desktop/cmd-file.txt");
        List<FullCommand> fullCommandList = ReadFile.fullCommandList;

        for (FullCommand command: fullCommandList){
            System.out.println(command);
        }

    }
}
