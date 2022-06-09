package game.match;

import command.FullCommand;
import player.Player;
import player.cards.Deck;

public class Match {

    private State canBet;
    private State canDealCards;
    private State canHoldCards;

    private State currentState;

    private Player player;
    private Deck deck;

    private boolean isDebugMode;

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
        try {
            this.currentState.bet(command);
        }catch (IllegalException i){
            System.out.println(i.getMessage());
        }
    }

    public void dealCards(){
        try {
            this.currentState.dealCards();
        }catch (IllegalException i){
            System.out.println(i.getMessage());
        }
    }
    public void holdCards(FullCommand command){
        try{
            this.currentState.holdCards(command);
        }catch (IllegalException i){
            System.out.println(i.getMessage());
        }
    }
    public void printAdvice() {
        System.out.println("falta o advice");
        /*
        try{

            this.currentState.printAdvice();
        }catch (IllegalException i){
            System.out.println(i.getMessage());
        }
        */
    }

    public int[] getAdvice(){
        //SO PODE SER CHAMADO NO ESTADO CanHoldCards
        //NUNCA RETORNA EXCEÇAO
        return new int[0];
    }

    public void printCredit() { this.currentState.printCredit(); }
    public void printStatistics() { this.currentState.printStatistics(); }

    public State getCanBet() { return this.canBet; }
    public State getCanDealCards() { return this.canDealCards; }
    public State getCanHoldCards() { return this.canHoldCards; }

    public Player getPlayer() { return this.player; }

    public Deck getDeck() { return this.deck; }

    public boolean getIsDebugMode() { return this.isDebugMode; }

    public void setState(State s) { this.currentState = s; }


}
