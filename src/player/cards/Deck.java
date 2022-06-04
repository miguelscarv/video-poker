package player.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> deck;

    public Deck(ArrayList<Card> d){
        this.deck = d;
    }

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

    public void shuffle(){
        Collections.shuffle(this.deck);
    }

    public void printCards(int n){
        if (this.deck.size() >= n) {
            for (int i = 0; i < n; i++) {
                System.out.println(String.format("Card nº%s: %s", i, this.deck.get(i)));
            }
        } else {
            System.out.println("The deck has less than " + n + " cards...");
        }
    }

    public void addCard(Card c){
        this.deck.add(c);
    }

    public Card popCard(){
        Card temp = this.deck.get(0);
        this.deck.remove(0);
        return temp;
    }

    public int getSize(){
        return this.deck.size();
    }

}
