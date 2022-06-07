package states;

import match.FullCommand;

public class CanBet implements State{

    Match match;

    public CanBet(Match match){
        this.match = match;
    }

    @Override
    public void bet(FullCommand command) {

        if(command.hasNumbers()) {

            if (command.getNumbers()[0] > 5 || command.getNumbers()[0] < 1) {
                System.out.println("Illegal amount to bet...");
            } else {
                //update players credit, update players lastBetAmount, update players hasBetBefore and change state
                this.match.player.setCredit(this.match.player.getCredit()-command.getNumbers()[0]);
                this.match.player.setHasBetBefore(true);
                this.match.player.setLastBetAmount(command.getNumbers()[0]);
                this.match.setState(this.match.getCanDealCards());
            }

        } else {
            //Is going to bet last amount bet or 5

            if (this.match.player.getHasBetBefore()){
                this.match.player.setCredit(this.match.player.getCredit()-this.match.player.getLastBetAmount());
            } else {
                this.match.player.setCredit(this.match.player.getCredit()-5);
                this.match.player.setHasBetBefore(true);
                this.match.player.setLastBetAmount(5);
            }
            this.match.setState(this.match.getCanDealCards());
        }
    }

    @Override
    public void printCredit() {
        System.out.println("The player\'s credit is: " + this.match.player.getCredit());
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

    @Override
    public void printStatistics() {

        System.out.println("Hand");
        System.out.println("------------------------------");

        System.out.println(String.format("Jacks or Better             %s", this.match.player.getStatistics().get("Jacks or Better")));
        System.out.println(String.format("Two Pair                    %s", this.match.player.getStatistics().get("Two Pair")));
        System.out.println(String.format("Three of a Kind             %s", this.match.player.getStatistics().get("Three of a Kind")));
        System.out.println(String.format("Straight                    %s", this.match.player.getStatistics().get("Straight")));
        System.out.println(String.format("Flush                       %s", this.match.player.getStatistics().get("Flush")));
        System.out.println(String.format("Full house                  %s", this.match.player.getStatistics().get("Full house")));
        System.out.println(String.format("Four of a Kind              %s", this.match.player.getStatistics().get("Four of a Kind")));
        System.out.println(String.format("Straight Flush              %s", this.match.player.getStatistics().get("Straight Flush")));
        System.out.println(String.format("Royal Flush                 %s", this.match.player.getStatistics().get("Royal Flush")));
        System.out.println(String.format("Other                       %s", this.match.player.getStatistics().get("Other")));

        System.out.println("------------------------------");
        int sum = 0;
        for (int value: this.match.player.getStatistics().values()) {
            sum += value;
        }
        System.out.println(String.format("Total                       %s", sum));
        System.out.println("------------------------------");

        float increase = (1 - (float) this.match.player.getCredit()/this.match.player.getInitialCredit())*100;
        System.out.println(String.format("Credit                 %s (%s%%)", this.match.player.getCredit(), increase));

    }
}

