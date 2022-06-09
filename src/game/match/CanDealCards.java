package game.match;

import command.FullCommand;

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
