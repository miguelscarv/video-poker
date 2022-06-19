package player;

import player.cards.Card;
import player.cards.Deck;

import java.util.HashMap;
import java.util.Map;

/**
 * Player Class represents a player.
 * A player can be seen as a hand with additional information regarding only the player: the current credit, the initial credit, the total amount bet, the total amount received in the end and the statistics.
 * @author Miguel Carvalho, Filipe Ferraz, João Baptista
 */
public class Player extends Hand {

    private int credit;
    private int initialCredit;
    private int totalAmountBet;
    private int totalAmountReceived;

    private int lastBetAmount;
    private boolean hasBetBefore;

    private Map<String, Integer> statistics = new HashMap<String, Integer>();

    /**
     * Constructor to initialize player object. Creates a player with initial credit and with empty list of cards.
     * @param credit Initial credit of the player.
     */
    public Player(int credit){
        super();
        this.credit = credit;
        this.initialCredit = credit;
        this.totalAmountBet = 0;
        this.initializeStatistics();
        this.hasBetBefore = false;
    }

    /**
     * This method returns all cards added to the thrown out pile.
     * @return Array with all cards of the thrown out pile.
     */
    //returns cards added to the thrown out pile
    public Card[] getThrownOutCards(){
        Card[] tempCards = new Card[this.thrownOutCards.size()];
        tempCards = this.thrownOutCards.toArray(tempCards);
        return tempCards;
    }

    /**
     * This method removes a specific card from the thrown out pile.
     * @param c Card that is being removed from the thrown out pile.
     */
    //remove a single card from the thrown out pile
    private void removeCardFromThrownOut(Card c){
        if (this.thrownOutCards.contains(c)){
            this.thrownOutCards.remove(c);
        }else {
            System.out.println("There is no " + c + " in the thrown out cards...");
        }
    }

    /**
     * This method removes all cards from thrown out pile and adds them to the end of the deck.
     * @param d Deck where the cards are being added.
     */
    //removes all cards from thrown out pile and adds them to deck
    private void addThrownOutCardsToDeck(Deck d){
        for(Card c: this.getThrownOutCards()){
            d.addCard(c);
            this.removeCardFromThrownOut(c);
        }
    }

    /**
     * This method add all the hand cards to the end of the deck after a play.
     * @param d Deck where the cards are being added.
     */
    //add hand cards to deck after a play - THIS IS ALWAYS CALLED AFTER A PLAY!!!!!!
    public void addHandCardsToDeck(Deck d) {
    	super.removeCardsFromHand(super.getHand());
        this.addThrownOutCardsToDeck(d);
    }

    /**
     * This method returns the player credit at a specific moment.
     * @return Credit of the player at a specific moment.
     */
    //get credit
    public int getCredit() { return this.credit; }

    /**
     * This method sets the player credit to a specific amount.
     * @param credit Amount set to the player credit.
     */
    public void setCredit(int credit) {
            this.credit = credit;
    }

    /**
     * This method returns the amount placed in the last bet. Only called when a bet already occurred previously.
     * @return amount placed in the last bet (1,2,3,4,5).
     */
    public int getLastBetAmount() { return this.lastBetAmount; }

    /**
     * This method sets the last bet amount placed. Called everytime a new valid bet occurs.
     * @param amount Amount placed in the last bet.
     */
    public void setLastBetAmount(int amount ) { this.lastBetAmount = amount;}

    /**
     * This method returns either true or false depending on if at a specific moment, a bet already occurred or not before.
     * @return boolean true/false depending on the outcome
     */
    public boolean getHasBetBefore() { return this.hasBetBefore; }

    /**
     * This method sets either true or false depending on if a bet already occurred or not.
     * Everytime a valid bet occurs, boolean is set to true.
     * @param b boolean set to true/false depending on the situation.
     */
    public void setHasBetBefore(boolean b) { this.hasBetBefore = b; }

    /**
     * This method returns the initial credit of the player.
     * @return Initial credit of the player.
     */
    public int getInitialCredit() { return this.initialCredit; }

    /**
     * This method returns the statistics of awarded (or not) hands hit until now.
     * @return List of hands and number of hits of each one until now.
     */
    public Map<String, Integer> getStatistics() { return this.statistics; }

    /**
     * This method initializes the statistics of the player. Called once at the beginning.
     */
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

    /**
     * This method adds a hit to a specific hand on the player statistics
     * @param s Specific hand hitted
     */
    public void addOneToStatistics(String s) {

        Integer tempInt = this.statistics.get(s);
        this.statistics.put(s,tempInt+1);
       
    }

    /**
     * This method adds to the total bet amount of the player the current bet amount.
     * Called everytime a valid bet occurred.
     * @param newBetAmount
     */
    public void addToTotalBetAmount(int newBetAmount){
        this.totalAmountBet += newBetAmount;
    }

    /**
     * This method returns the total bet amount of the player.
     * @return Total bet amount of the player.
     */
    public int getTotalAmountBet() { return this.totalAmountBet; }

    /**
     * This method returns the total amount gained by the player (current credit - initial credit).
     * @return Total amount gained.
     */
    public int getTotalAmountGained() { return this.getCredit() - this.getInitialCredit(); }
}
