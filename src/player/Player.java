package player;

import player.cards.Card;
import player.cards.Deck;


public class Player extends HandHelper{

    private int credit;
    public Player(int credit){
        super();
        this.credit = credit;
    }

    //returns cards added to the thrown out pile
    public Card[] getThrownOutCards(){
        Card[] tempCards = new Card[this.thrownOutCards.size()];
        tempCards = this.thrownOutCards.toArray(tempCards);
        return tempCards;
    }

    //remove a single card from the thrown out pile
    private void removeCardFromThrownOut(Card c){
        if (this.thrownOutCards.contains(c)){
            this.thrownOutCards.remove(c);
        }else {
            System.out.println("There is no " + c + " in the thrown out cards...");
        }
    }

    //removes all cards from thrown out pile and adds them to deck
    public void addThrownOutCardsToDeck(Deck d){
        for(Card c: this.getThrownOutCards()){
            d.addCard(c);
            this.removeCardFromThrownOut(c);
        }
    }
    
    //add hand to deck after a play and reshuffle the deck
    public void addHandToDeck(Deck d) {

    	while(this.hand.size() > 0) {
    		d.addCard(this.hand.get(0));
    		this.hand.remove(0);
    	}
    	d.shuffle();
    }
    
    //get credit
    public int getCredit() { return this.credit; }

    //change credit
    public void changeCredit(int creditChange) { this.credit += creditChange; }

    public void bet(int money) {
    	if(money > this.credit || money > 5) {
    		System.out.println("Illegal amount");
    	}else{
    		this.credit -= money;
    	}
    }
}
