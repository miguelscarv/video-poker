package player.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> deck;

    //creates a deck with given cards from card-file
    public Deck(ArrayList<Card> d){
        this.deck = d;
    }

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

    //shuffles deck
    public void shuffle(){
        Collections.shuffle(this.deck);
    }

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

    //adds a card to deck
    public void addCard(Card c){

        boolean hasCard = false;

        for (Card cInDeck : this.deck){
            if (c.equals(cInDeck)){
                hasCard = true;
            }
        }

        if (!hasCard) {
            this.deck.add(c);
        }else {
            System.out.println("This cards is already in the deck");
        }
    }

    //removes card from deck and returns it
    public Card popCard(){
        Card temp = this.deck.get(0);
        this.deck.remove(0);
        return temp;
    }

    //returns size of deck
    public int getSize(){
        return this.deck.size();
    }

}
