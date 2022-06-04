package main;

import match.ReadFile;
import player.cards.Deck;

public class Main {
    public static void main(String[] args){

        ReadFile.readCardFile("/Users/miguelcarvalho/Desktop/card-file.txt");
        Deck deck = new Deck(ReadFile.deck);

        deck.printCards(deck.getSize());
        deck.shuffle();

        System.out.println(deck.getSize());

    }
}
