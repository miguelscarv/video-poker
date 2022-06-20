package player.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Deck class represents a full deck of cards. As a full deck of cards, it has a list of cards.
 * @author Miguel Carvalho, Filipe Ferraz, Joao Baptista
 */
public class Deck {

    private List<Card> deck;

    /**
     * Constructor to initialize deck object from a card-file (Debug Mode).
     * @param d List of cards to constitute the deck.
     */
    //creates a deck with given cards from card-file
    public Deck(List<Card> d){
        this.deck = d;
    }

    /**
     * Constructor to initialize deck object with all the 52 cards (Simulation Mode).
     */
    //creates a deck with 52 cards from scratch
    public Deck(){
        this.deck = new ArrayList<Card>();

        for (Rank r:Rank.values()){
            for (Suit s: Suit.values()){
                Card tempCard = new Card(r,s);
                this.deck.add(tempCard);
            }
        }
        this.shuffle();
    }

    /**
     * This method shuffles the deck.
     */
    //shuffles deck
    public void shuffle(){
        Collections.shuffle(this.deck);
    }

    /**
     * This method prints n number of cards from the deck or none if the deck does not have n number of cards.
     * @param n Number of cards to be printed.
     */
    //prints n number of cards
    public void printCards(int n){
        if (this.deck.size() >= n) {
            for (int i = 0; i < n; i++) {
                System.out.println(String.format("Card nº%s: %s", i, this.deck.get(i)));
            }
        } else {
            System.out.println("The deck has less than " + n + " cards...");
        }
    }

    /**
     * This method adds a card to the end of the deck.
     * @param c Card that is added to the deck.
     */
    //adds a card to deck
    public void addCard(Card c){

        this.deck.add(c);

    }

    /**
     * This method removes a card from the beginning of the deck and returns it.
     * @return Card that is being removed from the deck.
     */
    //removes card from deck and returns it
    public Card popCard(){
        Card temp = this.deck.get(0);
        this.deck.remove(0);
        return temp;
    }

    /**
     * This methods returns the size of the deck.
     * @return Size of deck.
     */
    //returns size of deck
    public int getSize(){
        return this.deck.size();
    }

}
