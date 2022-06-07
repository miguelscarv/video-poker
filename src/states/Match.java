package states;

import match.FullCommand;
import player.Player;
import player.cards.Deck;

public class Match {

    State canBet;
    State canDealCards;
    State canHoldCards;

    State currentState;

    Player player;
    Deck deck;

    boolean isDebugMode;
    public Match(Player player, Deck deck, boolean isDebugMode){

        this.player = player;
        this.deck = deck;

        this.canBet = new CanBet(this);
        this.canDealCards = new CanDealCards(this);
        this.canHoldCards = new CanHoldCards(this);

        this.currentState = canBet;

        this.isDebugMode = isDebugMode;
    }

    public void bet(FullCommand command){
        this.currentState.bet(command);
    }

    public void dealCards(){
        this.currentState.dealCards();
    }
    public void holdCards(FullCommand command){
        this.currentState.holdCards(command);
    }
    public void printAdvice() { this.currentState.printAdvice(); }
    public void printCredit() { this.currentState.printCredit(); }
    public void printStatistics() { this.currentState.printStatistics(); }

    public State getCanBet() { return this.canBet; }
    public State getCanDealCards() { return this.canDealCards; }
    public State getCanHoldCards() { return this.canHoldCards; }

    void setState(State s) { this.currentState = s; }


}
