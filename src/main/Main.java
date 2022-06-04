package main;

import player.Player;
import player.cards.Card;
import player.cards.Deck;
import player.cards.Rank;
import player.cards.Suit;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args){

        Deck deck = new Deck();
        Player player = new Player(100);
        Card c = new Card(Rank.FOUR, Suit.DIAMONDS);

        player.initializeHand(deck);

        for(Card card: player.getHand()){
            System.out.println(card);
        }

        deck.addCard(c);

        System.out.println(deck.getSize());

    }
}
