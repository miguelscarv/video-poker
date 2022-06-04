package main;

import player.Player;
import player.cards.Card;
import player.cards.Deck;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args){

        Deck deck = new Deck();
        Player player = new Player(100);

        player.initializeHand(deck);
        Card[] cards = player.getHand();

        Card[] toRemove = new Card[2];
        toRemove[0] = cards[0];
        toRemove[1] = cards[1];

        System.out.println("\nThese are the cards in my hand:");

        for (Card c: cards){
            System.out.println(c);
        }

        System.out.println("\nThese are the cards I want to remove:");

        for (Card c: toRemove){
            System.out.println(c);
        }

        player.removeCardsFromHand(toRemove);

        System.out.println("\nThis is my hand after removing them:");

        for (Card c: player.getHand()){
            System.out.println(c);
        }

        player.addCardsToHand(deck);

        System.out.println("\nThis is my hand after refilling my cards:");

        for (Card c: player.getHand()){
            System.out.println(c);
        }

        System.out.println("\nNow the deck has " + deck.getSize() + " cards...");
        System.out.println("My hand has " + player.getHand().length + " cards...");
        System.out.println("The size of the thrownOutCards is " + player.getThrownOutCards().length);

        player.addThrownOutCardsBackToDeck(deck);

        System.out.println("\nAFTER ADDING THROWN OUT CARDS BACK TO THE DECK");
        System.out.println("Now the deck has " + deck.getSize() + " cards...");
        System.out.println("My hand has " + player.getHand().length + " cards...");
        System.out.println("The size of the thrownOutCards is " + player.getThrownOutCards().length);
        
        player.addHandToDeck(deck);
        
        System.out.println("Hand has: " + player.getHand().length + " cards");
        System.out.println("Deck has: " + deck.getSize() + " cards");
        



    }
}
