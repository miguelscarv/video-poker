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
        Player player = new Player(100);

        List<FullCommand> fullCommandList = Reader.readCommandFile("/Users/miguelcarvalho/Desktop/cmd-file.txt");

        Match match = new Match(player,deck);
        FullCommand command1 = new FullCommand(CommandType.BET);

        match.bet(command1);

        System.out.println("Players credit after betting with no number: " + player.getCredit());
        System.out.println("------------------------");


        match.deal();
        System.out.println("Hand before removing cards :");
        for(Card c: player.getHand()){
            System.out.println(c);
        }

        FullCommand command2 = new FullCommand(CommandType.HOLD);
        int[] toHold = {3,4,5};
        command2.addNumbers(toHold);
        match.hold(command2);

        System.out.println("Hand after removing cards :");
        for(Card c: player.getHand()){
            System.out.println(c);
        }

    }
}
