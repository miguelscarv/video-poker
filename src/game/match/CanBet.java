package game.match;

import command.FullCommand;
import player.Player;

/**
 * Class CanBet represents a state, in which the legal commands are: bet (b), credit ($) and statistics (s).
 * @author Miguel Carvalho, Filipe Ferraz, João Baptista
 */
public class CanBet extends State {
    /**
     * @see "game.match.State Constructor".
     * @param match Match associated with the state.
     */
    public CanBet(Match match){
        super(match);
    }

    /**
     * @see "game.match.State bet method"
     * At this state this method performs a bet, meaning the player information about its credit is updated.
     * @param command Full command with command type BET.
     * @throws IllegalException Exception Thrown when illegal command.
     */
    @Override
    public void bet(FullCommand command) throws IllegalException {

        Player player = super.match.getPlayer();

        if(command.hasNumbers()) {

            if (command.getNumbers()[0] > 5 || command.getNumbers()[0] < 1) {
                throw new IllegalException();
            } else {
                //update players credit, update players lastBetAmount, update players hasBetBefore and change state
                player.setCredit(player.getCredit()-command.getNumbers()[0]);
                player.addToTotalBetAmount(command.getNumbers()[0]);
                player.setHasBetBefore(true);
                player.setLastBetAmount(command.getNumbers()[0]);
                super.match.setState(super.match.getCanDealCards());
                if (this.match.getIsDebugMode()){
                    System.out.println("player is betting " + command.getNumbers()[0]);
                }
            }

        } else {
            //Is going to bet last amount bet or 5

            if (player.getHasBetBefore()){
                player.setCredit(player.getCredit()-player.getLastBetAmount());
                player.addToTotalBetAmount(player.getLastBetAmount());
                if (this.match.getIsDebugMode()){
                    System.out.println("player is betting " + player.getLastBetAmount());
                }
            } else {
                player.setCredit(player.getCredit()-5);
                player.addToTotalBetAmount(5);
                player.setHasBetBefore(true);
                player.setLastBetAmount(5);
                if (this.match.getIsDebugMode()){
                    System.out.println("player is betting 5");
                }
            }
            super.match.setState(super.match.getCanDealCards());
        }
    }

    /**
     * @see "game.match.State dealCards method".
     * At this state this method throws an illegal exception.
     * @throws IllegalException Exception Thrown when illegal command.
     */
    @Override
    public void dealCards() throws IllegalException {
        throw new IllegalException("d");
    }

    /**
     * @see "game.match.State holdCards method".
     * At this state this method throws an illegal exception.
     * @param command Full command with command type HOLD.
     * @throws IllegalException Exception Thrown when illegal command.
     */
    @Override
    public void holdCards(FullCommand command) throws IllegalException {
        throw new IllegalException("h");
    }

    /**
     * @see "game.match.State printAdvice method".
     * At this state this method throws an illegal exception.
     * @throws IllegalException Exception Thrown when illegal command.
     */
    @Override
    public void printAdvice() throws IllegalException {
        throw new IllegalException("a");
    }

}

