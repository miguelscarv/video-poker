package game.match;

import command.FullCommand;

import java.util.Arrays;

/**
 * Class CanDealCards represents a state, in which the legal commands are: deal (d), credit ($) and statistics (s).
 * @author Miguel Carvalho, Filipe Ferraz, João Baptista
 */

public class CanDealCards extends State {

    /**
     * @see "game.match.State Constructor".
     * @param match Match associated with the state.
     */
    public CanDealCards(Match match){
        super(match);
    }

    /**
     * @see "game.match.State bet method"
     * At this state this method throws an illegal exception.
     * @param command Full command with command type BET.
     * @throws IllegalException Exception Thrown when illegal command.
     */
    @Override
    public void bet(FullCommand command) throws IllegalException {
        throw new IllegalException("b");
    }

    /**
     * @see "game.match.State dealCards method".
     * At this state this method performs a deal, meaning five cards are retrieved from deck to the player hand.
     */
    @Override
    public void dealCards() {

        super.match.getPlayer().addCardsToHand(super.match.getDeck());
        super.match.setState(super.match.getCanHoldCards());

        if (this.match.getIsDebugMode()){
            System.out.println("player\'s hand " + Arrays.toString(this.match.getPlayer().getHand()));
        }
    }

    /**
     * @see "game.match.State holdCards method".
     * At this state this method throws an illegal exception
     * @param command Full command with command type HOLD.
     * @throws IllegalException Exception Thrown when illegal command.
     */
    @Override
    public void holdCards(FullCommand command) throws IllegalException {
        throw new IllegalException("h");
    }

    /**
     * @see "game.match.State printAdvice method".
     * At this state this method throws an illegal exception
     * @throws IllegalException Exception Thrown when illegal command.
     */
    @Override
    public void printAdvice() throws IllegalException {
        throw new IllegalException("a");
    }
}
