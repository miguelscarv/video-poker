package player;

import player.cards.Card;
import player.cards.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Hand Class represents a hand of a player. As a hand of a player, it contains a list of five cards.
 * @author Miguel Carvalho, Filipe Ferraz, João Baptista
 */
public class Hand {

    List<Card> thrownOutCards = new ArrayList<Card>();
    private List<Card> hand;

    /**
     * Constructor to initialize hand object. Creates an empty hand.
     */
    public Hand(){
        this.hand = new ArrayList<Card>();
    }

    /**
     * This method adds cards to hand from deck until hand has 5 cards.
     * @param d Deck from where the cards are being taken.
     */
    //adds cards to hand until hand has 5 cards
    public void addCardsToHand(Deck d){
        int handSize = this.hand.size();
        int deckSize = d.getSize();

        if (5-handSize > deckSize){
            System.out.println("There are not enough cards in the deck...");
        }else{
            for (int i = 0; i < 5 - handSize; i++){
                Card tempCard = d.popCard();
                this.hand.add(tempCard);
            }
        }
    }

    /**
     * This method removes specified cards from hand.
     * @param cards Cards that are being removed from hand.
     */
    //removes specified cards from hand
    public void removeCardsFromHand(Card[] cards){
        for (Card c: cards){
            this.thrownOutCards.add(c);
            this.hand.remove(c);
        }
    }

    /**
     * This method return all cards in hand.
     * @return Array with all cards of the hand.
     */
    //return all cards in hand
    public Card[] getHand(){
        Card[] tempCards = new Card[this.hand.size()];
        tempCards = this.hand.toArray(tempCards);
        return tempCards;
    }


}
