package Main;

import Player.Player;
import Player.cards.Deck;

public class Main {
    public static void main(String[] args){

        Deck deck = new Deck();
        Player player = new Player();

        deck.printCards(10);
        player.initHand(deck);
        System.out.println("--------------------");
        deck.printCards(10);


    }
}
