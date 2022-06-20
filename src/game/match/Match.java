package game.match;

import command.FullCommand;
import player.Player;
import player.cards.Deck;

/**
 * Match class represents the actual match of the game.
 * @author Miguel Carvalho, Filipe Ferraz, Joao Baptista
 */
public class Match {

    private State canBet;
    private State canDealCards;
    private State canHoldCards;

    private State currentState;

    private Player player;
    private Deck deck;

    private boolean isDebugMode;

    /**
     * Constructor to create a match
     * @param player Player of the match
     * @param deck Deck of the match
     * @param isDebugMode Boolean that represents whether the match is in Debug or Simulation mode
     */
    public Match(Player player, Deck deck, boolean isDebugMode){

        this.player = player;
        this.deck = deck;

        this.canBet = new CanBet(this);
        this.canDealCards = new CanDealCards(this);
        this.canHoldCards = new CanHoldCards(this);

        this.currentState = canBet;

        this.isDebugMode = isDebugMode;
    }

    /**
     * Method responsible for making a bet
     * @param command Command that represents a bet (may have the amount to bet)
     */
    public void bet(FullCommand command){
        try {
            this.currentState.bet(command);
        }catch (IllegalException i){
            System.out.println(i.getMessage());
        }
    }

    /**
     * Method responsible for executing a deal
     */
    public void dealCards(){
        try {
            this.currentState.dealCards();
        }catch (IllegalException i){
            System.out.println(i.getMessage());
        }
    }

    /**
     * Method responsible for holding the chosen cards 
     * @param command Command that has the indices of the cards to hold
     */
    public void holdCards(FullCommand command){
        try{
            this.currentState.holdCards(command);
        }catch (IllegalException i){
            System.out.println(i.getMessage());
        }
    }

    /**
     * Method that prints advice from following the strategy
     */
    public void printAdvice() {
        try{
            this.currentState.printAdvice();
        }catch (IllegalException i){
            System.out.println(i.getMessage());
        }

    }

    /**
     * Method that prints the available credit
     */
    public void printCredit() { this.currentState.printCredit(); }

    /**
     * Method that prints the statistics of the match
     */
    public void printStatistics() { this.currentState.printStatistics(); }

    /**
     * Getter of the attribute canBet
     * @return State in which it is possible to bet
     */
    public State getCanBet() { return this.canBet; }

    /**
     * Getter of the attribute canDealCards
     * @return State in which it is possible to deal
     */
    public State getCanDealCards() { return this.canDealCards; }

    /**
     * Getter of the attribute canHoldCards
     * @return State in which it is possible to hold hand cards
     */
    public State getCanHoldCards() { return this.canHoldCards; }

    /**
     * Getter of the attribute player
     * @return Player of the match
     */
    public Player getPlayer() { return this.player; }

    /**
     * Getter of the attribute deck
     * @return Deck of the match
     */
    public Deck getDeck() { return this.deck; }

    /**
     * Getter of the attribute isDebugMode
     * @return Whether the match is in debug or simulation mode
     */
    public boolean getIsDebugMode() { return this.isDebugMode; }

    /**
     * Changes the state(bet, deal, hold) in which the match is
     * @param s The new state
     */
    public void setState(State s) { this.currentState = s; }


}
