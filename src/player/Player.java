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
    
    //add hand to deck after a play and reshuffle the deck
    public void addHandToDeck(Deck d) {
    	while(this.hand.size() > 0) {
    		d.addCard(this.hand.get(0));
    		this.hand.remove(0);
    	}
    	d.shuffle();
    }
    
    public int getCredit() { return this.credit; }
    public void changeCredit(int creditChange) { this.credit += creditChange; }
    public void bet(int money) {
    	if(money > credit || money > 5) {
    		System.out.println("Illegal amount");
    	}else{
    		credit -= money;
    	}
    }




}
