package main;

import match.CommandType;
import match.FullCommand;
import match.Reader;
import player.Player;
import player.cards.*;
import player.cards.Deck;
import states.*;



import java.util.List;

public class Main {
    public static void main(String[] args){


        Deck deck = new Deck(Reader.readCardFile("/Users/miguelcarvalho/Desktop/card-file.txt"));
        Player player = new Player(10000);

        List<FullCommand> fullCommandList = Reader.readCommandFile("/Users/miguelcarvalho/Desktop/cmd-file.txt");

        Match match = new Match(player,deck,true);

        for(FullCommand command: fullCommandList){

            if (command.getCommand() == CommandType.BET){
                match.bet(command);
            } else if (command.getCommand() == CommandType.DEAL){
                match.dealCards();
            } else if (command.getCommand() == CommandType.HOLD){
                match.holdCards(command);
            } else if (command.getCommand() == CommandType.ADVICE){
                match.printAdvice();
            } else if (command.getCommand() == CommandType.CREDIT){
                match.printCredit();
            } else if (command.getCommand() == CommandType.STATISTICS){
                match.printStatistics();
            }
        }

    }
}
