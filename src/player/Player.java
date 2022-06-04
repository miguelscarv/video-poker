package player;

import player.cards.Card;
import player.cards.Deck;

public class Player extends HandHelper{

    private int credit;
    public Player(int credit){
        super();
        this.credit = credit;
    }

    public Card[] getThrownOutCards(){
        Card[] tempCards = new Card[this.thrownOutCards.size()];
        tempCards = this.thrownOutCards.toArray(tempCards);
        return tempCards;
    }

    public void removeCardFromThrownOut(Card c){
        if (this.thrownOutCards.contains(c)){
            this.thrownOutCards.remove(c);
        }else {
            System.out.println("There is no " + c + " in the thrown out cards...");
        }
    }

    public void addThrownOutCardsBackToDeck(Deck d){
        for(Card c: this.getThrownOutCards()){
            d.addCard(c);
            this.removeCardFromThrownOut(c);
        }
    }
    public int getCredit() { return this.credit; }
    public void setCredit(int newCredit) { this.credit = newCredit; }




}
