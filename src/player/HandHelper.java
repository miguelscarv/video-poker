package Player;

import Player.cards.Card;
import Player.cards.Deck;

import java.util.ArrayList;

public class HandHelper {

    private ArrayList<Card> thrownOutCards = new ArrayList<Card>();
    private ArrayList<Card> hand;

    public HandHelper(){
        this.hand = new ArrayList<Card>();
    }

    public void initHand(Deck d){

        for (int i = 0; i < 5 ; i++){
            Card tempCard = d.removeCard();
            this.hand.add(tempCard);
        }
    }

    public void removeCardsFromHand(Card[] cards){
        for (Card c: cards){
            this.thrownOutCards.add(c);
            this.hand.remove(c);
        }
    }

    public void addCardsToHand(Deck d){
        int handSize = this.hand.size();

        for (int i = 0; i < 5 - handSize; i++){
            Card tempCard = d.removeCard();
            this.hand.add(tempCard);
        }
    }

}
