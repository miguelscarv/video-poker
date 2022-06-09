package game.match;

import command.FullCommand;
import player.Player;

public class CanBet extends State {

    public CanBet(Match match){
        super(match);
    }

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
            }

        } else {
            //Is going to bet last amount bet or 5

            if (player.getHasBetBefore()){
                player.setCredit(player.getCredit()-player.getLastBetAmount());
                player.addToTotalBetAmount(player.getLastBetAmount());
            } else {
                player.setCredit(player.getCredit()-5);
                player.addToTotalBetAmount(5);
                player.setHasBetBefore(true);
                player.setLastBetAmount(5);
            }
            super.match.setState(super.match.getCanDealCards());
        }
    }

    @Override
    public void dealCards() throws IllegalException {
        throw new IllegalException("d");
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

