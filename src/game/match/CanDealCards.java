package game.match;

import command.FullCommand;

import java.util.Arrays;

public class CanDealCards extends State {


    public CanDealCards(Match match){
        super(match);
    }

    @Override
    public void bet(FullCommand command) throws IllegalException {
        throw new IllegalException("b");
    }

    @Override
    public void dealCards() {

        super.match.getPlayer().addCardsToHand(super.match.getDeck());
        super.match.setState(super.match.getCanHoldCards());

        if (this.match.getIsDebugMode()){
            System.out.println("player\'s hand " + Arrays.toString(this.match.getPlayer().getHand()));
        }
    }

    @Override
    public void holdCards(FullCommand command) throws IllegalException {
        throw new IllegalException("h");
    }

    @Override
    public void printAdvice() throws IllegalException {
        throw new IllegalException("a");
    }
}
