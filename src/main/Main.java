package main;

import match.ReadFile;
import player.cards.Deck;

public class Main {
    public static void main(String[] args){

        if(args[0].equals("-d")){
            //debug mode

        }else if (args[0].equals("-s")){
            //simulation mode

        }else{

            System.out.println("Invalid passing Arguments");
            System.exit(0);
        }

        ReadFile.readCardFile("/Users/miguelcarvalho/Desktop/card-file.txt");
        Deck deck = new Deck(ReadFile.deck);

        deck.printCards(deck.getSize());
        deck.shuffle();

        System.out.println(deck.getSize());

    }
}
