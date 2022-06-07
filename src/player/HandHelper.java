package player;

import player.cards.Card;
import player.cards.Deck;
import player.cards.Rank;
import player.cards.Suit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandHelper {

    List<Card> thrownOutCards = new ArrayList<Card>();
    private List<Card> hand;


    public HandHelper(){
        this.hand = new ArrayList<Card>();
    }

    //adds cards to hand until hand has 5 cards
    public void addCardsToHand(Deck d){
        int handSize = this.hand.size();

        for (int i = 0; i < 5 - handSize; i++){
            Card tempCard = d.popCard();
            this.hand.add(tempCard);
        }
    }


    //removes specified cards from hand
    public void removeCardsFromHand(Card[] cards){
        for (Card c: cards){
            this.thrownOutCards.add(c);
            this.hand.remove(c);
        }
    }

    //return all cards in hand
    public Card[] getHand(){
        Card[] tempCards = new Card[this.hand.size()];
        tempCards = this.hand.toArray(tempCards);
        return tempCards;
    }


}
