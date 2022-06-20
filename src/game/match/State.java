package game.match;

import command.FullCommand;
import player.Player;

/**
 * Abstract class State represents an abstract type that defines the possible actions at a certain part of the match.
 * @author Miguel Carvalho, Filipe Ferraz, Joao Baptista
 */
public abstract class State {

    Match match;

    /**
     * Constructor to initialize state object. Associates the state to a match.
     * @param m Match associated with the state.
     */
    public State(Match m){ this.match = m; }

    /**
     * This method performs a bet command (b).
     * @param command Full command with command type BET.
     * @throws IllegalException Exception Thrown when illegal command.
     */
    public abstract void bet(FullCommand command) throws IllegalException;

    /**
     * This method performs a credit command ($). Shows the current credit of the player.
     */
    public void printCredit(){
        System.out.println("player\'s credit is " + this.match.getPlayer().getCredit());
    }

    /**
     * This method performs a deal command (d).
     * @throws IllegalException Exception Thrown when illegal command.
     */
    public abstract void dealCards() throws IllegalException;

    /**
     * This method performs a hold command (h).
     * @param command Full command with command type HOLD.
     * @throws IllegalException Exception Thrown when illegal command.
     */
    public abstract void holdCards(FullCommand command) throws IllegalException;

    /**
     * This method performs an advice command (a).
     * @throws IllegalException Exception Thrown when illegal command.
     */
    public abstract void printAdvice() throws IllegalException;

    /**
     * This method performs a statistics command (s). Show the statistics from the player.
     */
    public void printStatistics(){

        Player player = this.match.getPlayer();
        
        System.out.println("------------------------------");
        System.out.println("Hand");
        System.out.println("------------------------------");

        System.out.println(String.format("Jacks or Better             %s", player.getStatistics().get("Jacks or Better")));
        System.out.println(String.format("Two Pair                    %s", player.getStatistics().get("Two Pair")));
        System.out.println(String.format("Three of a Kind             %s", player.getStatistics().get("Three of a Kind")));
        System.out.println(String.format("Straight                    %s", player.getStatistics().get("Straight")));
        System.out.println(String.format("Flush                       %s", player.getStatistics().get("Flush")));
        System.out.println(String.format("Full house                  %s", player.getStatistics().get("Full house")));
        System.out.println(String.format("Four of a Kind              %s", player.getStatistics().get("Four of a Kind")));
        System.out.println(String.format("Straight Flush              %s", player.getStatistics().get("Straight Flush")));
        System.out.println(String.format("Royal Flush                 %s", player.getStatistics().get("Royal Flush")));
        System.out.println(String.format("Other                       %s", player.getStatistics().get("Other")));

        System.out.println("------------------------------");
        int sum = 0;
        for (int value : player.getStatistics().values()) {
            sum += value;
        }

        System.out.println(String.format("Total                       %s", sum));
        System.out.println("------------------------------");

        float increase = ((float) (player.getTotalAmountGained()+player.getTotalAmountBet()) / player.getTotalAmountBet()) * 100;
        System.out.println(String.format("Credit                 %s (%s%%)", player.getCredit(), round(increase,2)));
        System.out.println("------------------------------");
    }
    private float round(float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }


}
