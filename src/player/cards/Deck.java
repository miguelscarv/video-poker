package Player.cards;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<Card> deck;

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
        for (int i = 0; i < n; i++){
            System.out.println(String.format("Card nº%s: %s", i, this.deck.get(i)));
        }
    }

    public void addCard(Card c){
        this.deck.add(c);
    }

    public Card removeCard(){
        Card temp = this.deck.get(0);
        this.deck.remove(0);
        return temp;
    }

    public void concatenateDecks(ArrayList<Card> d){
        this.deck.addAll(d);
    }

    public int getSize(){
        return this.deck.size();
    }

}
