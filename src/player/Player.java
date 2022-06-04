package player;

import player.cards.Card;

public class Player extends HandHelper{

    private int credit;
    public Player(int credit){

        super();
        this.credit = credit;
    }

    public Card[] getHand(){
        Card[] tempCards = new Card[this.hand.size()];
        tempCards = this.hand.toArray(tempCards);
        return tempCards;
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
    public int getCredit() { return this.credit; }
    public void setCredit(int newCredit) { this.credit = newCredit; }




}
