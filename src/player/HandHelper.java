package player;

import player.cards.Card;
import player.cards.Deck;

import java.util.ArrayList;
import java.util.List;

public class HandHelper {

    List<Card> thrownOutCards = new ArrayList<Card>();
    List<Card> hand;

    public HandHelper(){
        this.hand = new ArrayList<Card>();
    }

    public void initializeHand(Deck d){

        for (int i = 0; i < 5 ; i++){
            Card tempCard = d.popCard();
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
            Card tempCard = d.popCard();
            this.hand.add(tempCard);
        }
    }

}
