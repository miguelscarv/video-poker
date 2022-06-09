package player;

import player.cards.Card;
import player.cards.Deck;

import java.util.HashMap;
import java.util.Map;


public class Player extends Hand {

    private int credit;
    private int initialCredit;
    private int totalAmountBet;

    private int lastBetAmount;
    private boolean hasBetBefore;

    private Map<String, Integer> statistics = new HashMap<String, Integer>();

    public Player(int credit){
        super();
        this.credit = credit;
        this.initialCredit = credit;
        this.totalAmountBet = 0;
        this.initializeStatistics();
        this.hasBetBefore = false;
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
    private void addThrownOutCardsToDeck(Deck d){
        for(Card c: this.getThrownOutCards()){
            d.addCard(c);
            this.removeCardFromThrownOut(c);
        }
    }
    
    //add hand cards to deck after a play - THIS IS ALWAYS CALLED AFTER A PLAY!!!!!!
    public void addHandCardsToDeck(Deck d) {
    	super.removeCardsFromHand(super.getHand());
        this.addThrownOutCardsToDeck(d);
    }
    
    //get credit
    public int getCredit() { return this.credit; }
    public void setCredit(int credit) {
        if (this.credit+credit > 0) {
            this.credit = credit;
        } else {
            System.out.println("The player only has " + this.credit + " credit....\nCredit can\'t be negative");
            System.exit(0);
        }
    }
    public int getLastBetAmount() { return this.lastBetAmount; }
    public void setLastBetAmount(int amount ) { this.lastBetAmount = amount;}
    public boolean getHasBetBefore() { return this.hasBetBefore; }
    public void setHasBetBefore(boolean b) { this.hasBetBefore = b; }
    public int getInitialCredit() { return this.initialCredit; }

    public Map<String, Integer> getStatistics() { return this.statistics; }


    private void initializeStatistics(){

        this.statistics.put("Jacks or Better", 0);
        this.statistics.put("Two Pair", 0);
        this.statistics.put("Three of a Kind", 0);
        this.statistics.put("Straight", 0);
        this.statistics.put("Flush", 0);
        this.statistics.put("Full house", 0);
        this.statistics.put("Four of a Kind", 0);
        this.statistics.put("Straight Flush", 0);
        this.statistics.put("Royal Flush", 0);
        this.statistics.put("Other", 0);

    }

    public void addOneToStatistics(String s) {

        Integer tempInt = this.statistics.get(s);
        this.statistics.put(s,tempInt+1);
       
    }

    public void addToTotalBetAmount(int newBetAmount){
        this.totalAmountBet += newBetAmount;
    }

    public int getTotalAmountBet() { return this.totalAmountBet; }


}
