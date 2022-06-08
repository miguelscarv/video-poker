package states;

import match.FullCommand;

public class CanBet extends State{


    public CanBet(Match match){
        super(match);
    }

    @Override
    public void bet(FullCommand command) {

        if(command.hasNumbers()) {

            if (command.getNumbers()[0] > 5 || command.getNumbers()[0] < 1) {
                System.out.println("Illegal amount to bet: " + command.getNumbers()[0] + " credits");
            } else {
                //update players credit, update players lastBetAmount, update players hasBetBefore and change state
                super.match.player.setCredit(super.match.player.getCredit()-command.getNumbers()[0]);
                super.match.player.setHasBetBefore(true);
                super.match.player.setLastBetAmount(command.getNumbers()[0]);
                super.match.setState(super.match.getCanDealCards());
            }

        } else {
            //Is going to bet last amount bet or 5

            if (super.match.player.getHasBetBefore()){
                super.match.player.setCredit(super.match.player.getCredit()-super.match.player.getLastBetAmount());
            } else {
                super.match.player.setCredit(super.match.player.getCredit()-5);
                super.match.player.setHasBetBefore(true);
                super.match.player.setLastBetAmount(5);
            }
            super.match.setState(super.match.getCanDealCards());
        }
    }

    @Override
    public void dealCards() {
        System.out.println("d: illegal command");
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

