package states;

import match.FullCommand;

public class CanDealCards extends State{


    public CanDealCards(Match match){
        super(match);
    }

    @Override
    public void bet(FullCommand command) {
        System.out.println("b: illegal command");
    }

    @Override
    public void dealCards() {
        super.match.player.addCardsToHand(super.match.deck);
        super.match.setState(super.match.getCanHoldCards());
    }

    @Override
    public void holdCards(FullCommand command) {
        System.out.println("h: illegal command");
    }

    @Override
    public void printAdvice() {
        System.out.println("a: illegal command");
    }
}
